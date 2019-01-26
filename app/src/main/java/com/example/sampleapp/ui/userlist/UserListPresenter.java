package com.example.sampleapp.ui.userlist;

import android.util.Log;

import com.example.sampleapp.R;
import com.example.sampleapp.data.DataManager;
import com.example.sampleapp.data.db.model.User;
import com.example.sampleapp.ui.base.BasePresenter;
import com.example.sampleapp.utils.AppConstants;
import com.example.sampleapp.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.processors.PublishProcessor;

public class UserListPresenter<V extends UserListMvpView> extends BasePresenter<V>
        implements UserListMvpPresenter<V>, UserListAdapter.Callback {

    private static final String TAG = UserListPresenter.class.getSimpleName();

    private static final int ITEM_VISIBLE_BUFFER_LIMIT = 1;
    private int pageNumber, totalPageCount;

    /*
    *   Flag to help toggle the Progress Dialog at the bottom of the List View
    * */
    private boolean isLoading;


    private PublishProcessor<Integer> paginator;

    @Inject
    public UserListPresenter(SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable,
                             DataManager dataManager) {
        super(schedulerProvider, compositeDisposable, dataManager);
        paginator = PublishProcessor.create();
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        fetchDataSet();
        getMoreNetworkResults();
    }

    /*
    *   This method takes care of managing results from 2 data sources (ie. DB and Network)
    *
    *   The Subscriber is going to take whichever events come first, but if the network
    *   observable starts emitting first, then the disk results will be ignored (i.e takeUntil())
    * */
    private void fetchDataSet() {
        getMvpView().removeScrollListener();
        getNetworkResults()
                .publish(networkObservable ->
                        Observable.merge(
                                networkObservable,
                                getDiskResults().takeUntil(networkObservable)))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new DisposableObserver<List<User>>() {
                    @Override
                    public void onNext(List<User> users) {
                        if (!isViewAttached())
                            return;

                        if (users != null && users.size() > 0) {
                            getMvpView().updateProgressBarVisibility(isLoading = false);
                            getMvpView().updateUserList(users);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (!isViewAttached())
                            return;

                        getMvpView().addScrollListener();
                        getMvpView().updateProgressBarVisibility(isLoading = false);
                    }
                });
    }

    /*
    *   Returns the results from the API call and updates the DB on success.
    *
    *   Also before adding to the DB, the records are cleared and fresh results are stored.
    * */
    private Observable<List<User>> getNetworkResults() {
        getMvpView().updateProgressBarVisibility(isLoading = true);

        return getDataManager().
                doUserListRequest(AppConstants.PAGE_START_INDEX, AppConstants.PER_PAGE_MAX_COUNT)
                .doOnNext(userListResponse -> {
                    if (userListResponse != null && userListResponse.getTotalPages() != 0) {
                        totalPageCount = userListResponse.getTotalPages();
                    }
                    pageNumber = AppConstants.PAGE_START_INDEX;
                })
                .doOnError(throwable -> {
                    if (isViewAttached()) {
                        getMvpView().isNetworkConnected();
                        getMvpView().updateProgressBarVisibility(isLoading = false);
                    }
                    Log.e(TAG, throwable.toString());
                })
                //Transforms the Response object to the User List
                .flatMap(userListResponse -> {
                    if (userListResponse != null && userListResponse.getData() != null
                            && userListResponse.getData().size() > 0)
                        return Observable.just(userListResponse.getData());
                    else return Observable.empty();
                })
                //Clear Old Records before Adding New Records. Concat ensures order.
                .flatMap(users -> {
                    return Observable.concat(
                            clearAllUsersObservable(),
                            insertAllUsersObservable(users))
                            .doOnError(throwable -> Log.d(TAG, throwable.toString()))
                            .ignoreElements()
                            .andThen(Observable.just(users));
                })
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui());
    }


    /*
    *   Returns all the Database records wrapped in an Observable
    *
    *   This results will be used until fresh results comes in from the network or
    *   when the app is offline.
    * */
    private Observable<List<User>> getDiskResults() {
        return getDataManager().getAllUsers()
                .subscribeOn(getSchedulerProvider().io());
    }

    private Observable clearAllUsersObservable() {
        return getDataManager().wipeUserData().subscribeOn(getSchedulerProvider().io()).toObservable();
    }

    private Observable insertAllUsersObservable(List<User> users) {
        return getDataManager().insertUserList(users).subscribeOn(getSchedulerProvider().io());
    }


    /*
    *   This method employs the logic to initiate the API call when the bottom of the list is reached.
    *
    *   There are essentially two checks happening here
    *   1. The first if is to check if the second last item of the list is reached. The buffer is kept at 1.
    *   2. The second if is to check if the next Paging Results are not already in the process of being fetched.
    *       And it also checks to not exceed the total Paging Limit.
    * */
    @Override
    public void onScrolledPositionUpdate(int lastVisibleItemPosition, int itemCount) {
        if (itemCount <= lastVisibleItemPosition + ITEM_VISIBLE_BUFFER_LIMIT) {
            if (!isLoading && pageNumber < totalPageCount) {
                paginator.onNext(pageNumber + 1);
                getMvpView().updateProgressBarVisibility(isLoading = true);
            }
        }
    }


    /*
    *   The Subject created for Pagination acts as an Observer when paginator.onNext() is triggered.
    *   This subject will also behave as an Observable, and help us shape the stream by making the API call
    *   with the page Number information coming from paginator.onNext()
    *
    *   Once we receive new Paging results, it adds to the existing list in the UI and also makes new DB entries.
    * */
    private void getMoreNetworkResults() {
        getCompositeDisposable().add(
                paginator.onBackpressureDrop()
                        .concatMap(pageNo -> getDataManager()
                                .doUserListRequest(pageNo, AppConstants.PER_PAGE_MAX_COUNT)
                                .subscribeOn(getSchedulerProvider().io())
                                .toFlowable(BackpressureStrategy.DROP))
                        //Adds the fetched records into the DB
                        .flatMap(userListResponse -> {
                            pageNumber++;
                            return insertAllUsersObservable(userListResponse.getData())
                                    .ignoreElements().andThen(Flowable.just(userListResponse));

                        })
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(userListResponse -> {
                            if (!isViewAttached())
                                return;


                            if (userListResponse != null && userListResponse.getData() != null
                                    && userListResponse.getData().size() > 0) {
                                getMvpView().appendToUserList(userListResponse.getData());
                                getMvpView().updateProgressBarVisibility(isLoading = false);
                            }
                        }, throwable -> {
                            if (!isViewAttached())
                                return;

                            if (getMvpView().isNetworkConnected())
                                getMvpView().showMessage(R.string.something_went_wrong);
                            getMvpView().updateProgressBarVisibility(isLoading = false);
                        })
        );
    }

    @Override
    public void retryInternet() {
        if (isViewAttached() && getMvpView().isNetworkConnected())
            fetchDataSet();
    }
}
