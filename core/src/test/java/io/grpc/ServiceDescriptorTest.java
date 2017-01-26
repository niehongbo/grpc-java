/*
 * Copyright 2017, Google Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 *    * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.grpc;

import io.grpc.MethodDescriptor.MethodType;
import io.grpc.testing.TestMethodDescriptors;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ServiceDescriptor}.
 */
@RunWith(JUnit4.class)
public class ServiceDescriptorTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void failsOnNullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");

    new ServiceDescriptor(null, Collections.emptyList());
  }

  @Test
  public void failsOnNullMethods() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("methods");

    new ServiceDescriptor("name", (Collection<MethodDescriptor<?, ?>>)null);
  }

  @Test
  public void failsOnNonMatchingNames() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("service names");

    new ServiceDescriptor("name", Collections.<MethodDescriptor<?, ?>>singletonList(
        MethodDescriptor.create(
            MethodType.UNARY,
            MethodDescriptor.generateFullMethodName("wrongservice", "method"),
            TestMethodDescriptors.noopMarshaller(),
            TestMethodDescriptors.noopMarshaller())));
  }

  @Test
  public void failsOnNonDuplicateNames() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("duplicate");

    new ServiceDescriptor("name", Arrays.<MethodDescriptor<?, ?>>asList(
        MethodDescriptor.create(
            MethodType.UNARY,
            MethodDescriptor.generateFullMethodName("name", "method"),
            TestMethodDescriptors.noopMarshaller(),
            TestMethodDescriptors.noopMarshaller()),
        MethodDescriptor.create(
            MethodType.UNARY,
            MethodDescriptor.generateFullMethodName("name", "method"),
            TestMethodDescriptors.noopMarshaller(),
            TestMethodDescriptors.noopMarshaller())));
  }
}