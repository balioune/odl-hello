/*
 * Copyright © 2015 Alioune, BA. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.hello.impl;

import com.google.common.util.concurrent.FutureCallback;

import org.slf4j.Logger;

public class LoggingFuturesCallBack<V> implements FutureCallback<V> {

    private Logger LOG;
    private String message;

    public LoggingFuturesCallBack(String message,Logger LOG) {
        this.message = message;
        this.LOG = LOG;
    }

    @Override
    public void onFailure(Throwable e) {
        LOG.warn(message,e);

    }

    @Override
    public void onSuccess(V arg0) {
        LOG.info("Success! {} ", arg0);

    }

}