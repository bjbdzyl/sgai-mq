package com.szx.ea.mq.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("com.szx.ea.mq.annotations.SubscribeMq")
//@SupportedOptions(value = {"eventBusIndex", "verbose"})
public class AptSubscribeMq extends AbstractProcessor {

    //private final ListMap<TypeElement, ExecutableElement> methodsByClass = new ListMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Set<? extends TypeElement> elements = roundEnv.getElementsAnnotatedWith(SubscribeMq.class);
        //processingEnv.getFiler().createSourceFile()
        collectSubscribers(annotations,roundEnv, processingEnv.getMessager());
        return false;
    }

    private void collectSubscribers(Set<? extends TypeElement> annotations, RoundEnvironment env, Messager messager) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = env.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element instanceof ExecutableElement) {
                    ExecutableElement method = (ExecutableElement) element;
                    if (checkHasNoErrors(method, messager)) {
                        TypeElement classElement = (TypeElement) method.getEnclosingElement();
                        //methodsByClass.putElement(classElement, method);
                    }
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "@Subscribe is only valid for methods", element);
                }
            }
        }
    }

    private boolean checkHasNoErrors(ExecutableElement element, Messager messager) {
        if (element.getModifiers().contains(Modifier.STATIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber method must not be static", element);
            return false;
        }

        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber method must be public", element);
            return false;
        }

        List<? extends VariableElement> parameters = ((ExecutableElement) element).getParameters();
        if (parameters.size() != 1) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber method must have exactly 1 parameter", element);
            return false;
        }
        return true;
    }
}
