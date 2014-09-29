/*
 * Copyright (c) 2014 Spotify AB.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.helios.common.context;

import java.util.concurrent.Callable;

/**
 * A Callable that will include additional stack trace information if run() throws an
 * Exception.
 */
final class ContextCallable<T> implements Callable<T> {
  private final Callable<T> task;
  private final StackTraceElement[] trace;

  ContextCallable(final Callable<T> task) {
    this.task = task;
    this.trace = Context.getStackContext();
  }

  @Override
  public T call() throws Exception {
    try {
      return task.call();
    } catch (final Throwable th) {
      Context.handleException(trace, th);
      throw th;
    }
  }
}