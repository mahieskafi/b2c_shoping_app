package com.srp.eways.util.rx;

import io.reactivex.Scheduler;

/**
 * Created by Eskafi on 8/3/2019.
 */
public interface SchedulerProvider {

    Scheduler computation();

    Scheduler io();

    Scheduler ui();
}
