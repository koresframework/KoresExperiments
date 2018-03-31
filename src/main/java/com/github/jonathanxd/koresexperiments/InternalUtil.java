/*
 *      KoresExperiments - CodeAPI Bytecode Experiments! <https://github.com/JonathanxD/CodeProxy>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2018 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.koresexperiments;

import com.github.jonathanxd.kores.bytecode.processor.BytecodeGenerator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class InternalUtil {
    static final int VIRTUAL = 0;
    static final int STATIC = 2;
    static final int NORMAL = 0;
    static final int DYNAMIC = 1;

    private static final String BASE_GEN_PACKAGE = "com.github.jonathanxd.koresexperiments.generated.experiment";
    private static final ThreadLocal<BytecodeGenerator> GENERATOR = ThreadLocal.withInitial(BytecodeGenerator::new);
    private static AtomicInteger count = new AtomicInteger(0);


    /**
     * Creates class name for a class to be generated by {@code module}.
     *
     * @param module Module generating the class.
     * @param base   Base name to append to it.
     * @return Name for a new experiment generated class.
     */
    static String createGenClassName(String module, String base) {
        return BASE_GEN_PACKAGE + "." + module + "." + base + "$" + count.getAndIncrement();
    }

    /**
     * Gets the thread local {@link BytecodeGenerator}.
     *
     * @return Thread local bytecode generator.
     */
    static BytecodeGenerator getThreadBytecodeGenerator() {
        return GENERATOR.get();
    }

    /**
     * Loop all unique methods of {@code itfs}, consume them with {@code each} and add to a list.
     *
     * Methods are identified by name, return type and parameters type, declaring class is ignored.
     *
     * @param itfs Interfaces with methods.
     * @param each Consumer of unique methods.
     * @return A set with unique methods.
     */
    static Set<Method> loopMethods(Collection<? extends Class<?>> itfs, Consumer<Method> each) {
        Set<Method> methodsToImplement = new HashSet<>();

        for (Class<?> itf : itfs) {
            for (Method method : itf.getDeclaredMethods()) {
                if (Modifier.isAbstract(method.getModifiers())) {
                    if (Util.contains(methodsToImplement, method))
                        continue;
                    each.accept(method);
                    methodsToImplement.add(method);
                }
            }
        }

        return methodsToImplement;
    }
}