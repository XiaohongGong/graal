/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2018, Arm Limited and affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.graalvm.compiler.core.aarch64.test;

import org.graalvm.compiler.lir.LIRInstruction;
import org.graalvm.compiler.lir.aarch64.AArch64ArithmeticOp;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;

public class AArch64MultiplyAddSubTest extends AArch64MatchRuleTest {
    private static final Predicate<LIRInstruction> predicate = op -> (op instanceof AArch64ArithmeticOp.MultiplyAddSubOp);

    /**
     * multiplyAddSub match rule test for add operation with int type.
     */
    private static int mulAddInt(int input0, int input1, int input2) {
        return input2 + input0 * input1;
    }

    @Test
    public void testMultiplyAddInt() {
        // To make sure the private function "mulAddInt" be used locally, we need to call it
        // directly at least once. So we didn't call "test()" to do the testing for the first
        // time.
        int expected = mulAddInt(3, 46, 23);
        Result result = executeActual(getResolvedJavaMethod("mulAddInt"), null, 3, 46, 23);
        int actual = (int) result.returnValue;
        Assert.assertEquals(expected, actual);

        test("mulAddInt", -3, -5, 6);
        test("mulAddInt", Integer.MAX_VALUE, 2, 5);
        checkLIR("mulAddInt", predicate, 1);
    }

    /**
     * multiplyAddSub match rule test for add operation with long type.
     */
    private static long mulAddLong(long input0, long input1, long input2) {
        return input0 * input1 + input2;
    }

    @Test
    public void testMultiplyAddLong() {
        long expected = mulAddLong(43, 46442, 2341455);
        Result result = executeActual(getResolvedJavaMethod("mulAddLong"), null, 43L, 46442L, 2341455L);
        long actual = (long) result.returnValue;
        Assert.assertEquals(expected, actual);

        test("mulAddLong", -3141L, -542324L, 65225L);
        test("mulAddLong", Long.MAX_VALUE, 2L, 124454L);
        checkLIR("mulAddLong", predicate, 1);
    }

    /**
     * multiplyAddSub match rule test for sub operation with int type.
     */
    private static int mulSubInt(int input0, int input1, int input2) {
        return input2 - input0 * input1;
    }

    @Test
    public void testMultiplySubInt() {
        int expected = mulSubInt(3, 46, 23);
        Result result = executeActual(getResolvedJavaMethod("mulSubInt"), null, 3, 46, 23);
        int actual = (int) result.returnValue;
        Assert.assertEquals(expected, actual);

        test("mulSubInt", -5, 4, -3);
        test("mulSubInt", Integer.MIN_VALUE, 2, Integer.MAX_VALUE);
        checkLIR("mulSubInt", predicate, 1);
    }

    /**
     * multiplyAddSub match rule test for sub operation with long type.
     */
    private static long mulSubLong(long input0, long input1, long input2) {
        return input2 - input0 * input1;
    }

    @Test
    public void testMultiplySubLong() {
        long expected = mulSubLong(43, 46442, 2341455);
        Result result = executeActual(getResolvedJavaMethod("mulSubLong"), null, 43L, 46442L, 2341455L);
        long actual = (long) result.returnValue;
        Assert.assertEquals(expected, actual);

        test("mulSubLong", -3141L, 542324L, -65225L);
        test("mulSubLong", Long.MIN_VALUE, 2L, Long.MAX_VALUE);
        checkLIR("mulSubLong", predicate, 1);
    }
}
