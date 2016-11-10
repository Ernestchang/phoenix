package com.yulin.act.page.base;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class BaseViewModel {

    private List<Subscription> mSubscriptions = new ArrayList<>();

    protected void addSubscription(Subscription subscription) {
        if (mSubscriptions != null) {
            mSubscriptions.add(subscription);
        }
    }

    public void clearSubscription() {
        if (mSubscriptions != null) {
            for (Subscription subscription : mSubscriptions) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        }

        mSubscriptions = null;
    }

}
