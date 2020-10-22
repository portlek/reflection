/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.reflection.clazz;

import io.github.portlek.reflection.*;
import io.github.portlek.reflection.constructor.ConstructorOf;
import io.github.portlek.reflection.field.FieldOf;
import io.github.portlek.reflection.method.MethodOf;
import io.github.portlek.reflection.parameterized.ParameterizedOf;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * an implementation for {@link RefClass}.
 *
 * @param <T> the class's type.
 */
public final class ClassOf<T> implements RefClass<T> {

  /**
   * the class.
   */
  @NotNull
  private final Class<T> clazz;

  /**
   * ctor.
   *
   * @param clazz the class.
   */
  public ClassOf(@NotNull final Class<T> clazz) {
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public ClassOf(@NotNull final T object) {
    this((Class<T>) object.getClass());
  }

  @SuppressWarnings("unchecked")
  public ClassOf(@NotNull final String classname) throws ClassNotFoundException {
    this((Class<T>) Class.forName(classname));
  }

  @NotNull
  @Override
  public Class<T> getRealClass() {
    return this.clazz;
  }

  @Override
  public boolean isInstance(@NotNull final Object object) {
    return this.clazz.isInstance(object);
  }

  @NotNull
  @Override
  public Optional<RefMethod> getPrimitiveMethod(@NotNull final String name, @NotNull final Object... types) {
    return this.getMethod0(name, true, types);
  }

  @NotNull
  @Override
  public Optional<RefMethod> getMethod(@NotNull final String name, @NotNull final Object... types) {
    return this.getMethod0(name, false, types);
  }

  @Override
  @NotNull
  public Optional<RefMethod> getPrimitiveMethodByParameter(@NotNull final Object... types) {
    return this.findMethod0(true, types);
  }

  @Override
  @NotNull
  public Optional<RefMethod> getMethodByParameter(@NotNull final Object... types) {
    return this.findMethod0(false, types);
  }

  @NotNull
  @Override
  public Optional<RefMethod> getMethodByName(@NotNull final String... names) {
    final Collection<Method> methods = new ArrayList<>(Arrays.asList(this.clazz.getMethods()));
    methods.addAll(Arrays.asList(this.clazz.getDeclaredMethods()));
    return methods.stream()
      .filter(Objects::nonNull)
      .filter(method ->
        Arrays.stream(names)
          .findFirst()
          .map(name -> method.getName().equals(name))
          .orElse(false))
      .findFirst()
      .map(MethodOf::new);
  }

  @NotNull
  @Override
  public <X> Optional<RefMethod> getMethodByReturnType(@NotNull final RefClass<X> type) {
    return this.getMethodByReturnType(type.getRealClass());
  }

  @NotNull
  @Override
  public Optional<RefMethod> getMethodByReturnType(@NotNull final Class<?> type) {
    final Collection<Method> methods = new ArrayList<>(Arrays.asList(this.clazz.getMethods()));
    methods.addAll(Arrays.asList(this.clazz.getDeclaredMethods()));
    return methods.stream()
      .filter(Objects::nonNull)
      .filter(method -> type.equals(method.getReturnType()))
      .findFirst()
      .map(MethodOf::new);
  }

  @NotNull
  @Override
  public Optional<RefConstructed<T>> getPrimitiveConstructor(@NotNull final Object... types) {
    return this.getConstructor0(true, types);
  }

  @NotNull
  @Override
  public Optional<RefConstructed<T>> getConstructor(@NotNull final Object... types) {
    return this.getConstructor0(false, types);
  }

  @SuppressWarnings("unchecked")
  @NotNull
  @Override
  public Optional<RefConstructed<T>> getConstructor(final int number) {
    final Collection<Constructor<?>> constructors = new ArrayList<>(Arrays.asList(this.clazz.getConstructors()));
    constructors.addAll(Arrays.asList(this.clazz.getDeclaredConstructors()));
    return constructors.stream()
      .filter(Objects::nonNull)
      .filter(constructor -> constructor.getParameterTypes().length == number)
      .findFirst()
      .map(constructor -> new ConstructorOf<>((Constructor<T>) constructor));
  }

  @NotNull
  @Override
  public Optional<RefField> getField(@NotNull final String name) {
    try {
      return Optional.of(new FieldOf(this.clazz.getField(name)));
    } catch (final NoSuchFieldException ignored) {
      try {
        return Optional.of(new FieldOf(this.clazz.getDeclaredField(name)));
      } catch (final NoSuchFieldException e) {
        return Optional.empty();
      }
    }
  }

  @NotNull
  @Override
  public <X> Optional<RefField> getField(@NotNull final RefClass<X> type) {
    return this.getField(type.getRealClass());
  }

  @NotNull
  @Override
  public Optional<RefField> getField(final @NotNull Class<?> type) {
    final List<RefField> fields = this.getFields();
    fields.addAll(this.getDeclaredFields());
    return fields.stream()
      .filter(Objects::nonNull)
      .filter(field -> type.equals(field.getType()))
      .findFirst();
  }

  @NotNull
  @Override
  public List<RefField> getFields() {
    return Arrays.stream(this.clazz.getFields())
      .map(FieldOf::new)
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<RefField> getDeclaredFields() {
    return Arrays.stream(this.clazz.getDeclaredFields())
      .map(FieldOf::new)
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<RefMethod> getMethods() {
    return Arrays.stream(this.clazz.getMethods())
      .map(MethodOf::new)
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<RefMethod> getDeclaredMethods() {
    return Arrays.stream(this.clazz.getDeclaredMethods())
      .map(MethodOf::new)
      .collect(Collectors.toList());
  }

  @Override
  public <A extends Annotation> Optional<A> getAnnotation(@NotNull final Class<A> annotationClass) {
    return Optional.ofNullable(this.clazz.getDeclaredAnnotation(annotationClass));
  }

  @NotNull
  private Optional<RefMethod> getMethod0(@NotNull final String name, final boolean primitive,
                                         @NotNull final Object... types) {
    final RefParameterized<RefMethod> parameter = new ParameterizedOf<>(primitive, types);
    return parameter.apply(classes -> {
      try {
        return Optional.of(new MethodOf(this.clazz.getMethod(name, classes)));
      } catch (final NoSuchMethodException e) {
        return parameter.apply(declaredclasses -> {
          try {
            return Optional.of(new MethodOf(this.clazz.getDeclaredMethod(name, declaredclasses)));
          } catch (final NoSuchMethodException noSuchMethodException) {
            return Optional.empty();
          }
        });
      }
    });
  }

  @NotNull
  private Optional<RefConstructed<T>> getConstructor0(final boolean primitive, @NotNull final Object... types) {
    final RefParameterized<RefConstructed<T>> parameter = new ParameterizedOf<>(primitive, types);
    return parameter.apply(classes -> {
      try {
        return Optional.of(new ConstructorOf<>(this.clazz.getConstructor(classes)));
      } catch (final NoSuchMethodException e) {
        return parameter.apply(declaredClasses -> {
          try {
            return Optional.of(new ConstructorOf<>(this.clazz.getDeclaredConstructor(declaredClasses)));
          } catch (final NoSuchMethodException noSuchMethodException) {
            return Optional.empty();
          }
        });
      }
    });
  }

  @NotNull
  private Optional<RefMethod> findMethod0(final boolean primitive, @NotNull final Object... types) {
    final RefParameterized<RefMethod> parameter = new ParameterizedOf<>(primitive, types);
    final Collection<Method> methods = new ArrayList<>(Arrays.asList(this.clazz.getMethods()));
    methods.addAll(Arrays.asList(this.clazz.getDeclaredMethods()));
    final Collection<Class<?>> classList = new ArrayList<>();
    parameter.apply(classes -> {
      classList.addAll(Arrays.asList(classes));
      return Optional.empty();
    });
    findMethod:
    for (final Method method : methods) {
      final Class<?>[] methodtypes = method.getParameterTypes();
      if (methodtypes.length != classList.size()) {
        continue;
      }
      for (int index = 0; index < classList.size(); index++) {
        if (!Arrays.equals(classList.toArray(new Class<?>[0]), methodtypes)) {
          continue findMethod;
        }
      }
      return Optional.of(new MethodOf(method));
    }
    return Optional.empty();
  }
}
