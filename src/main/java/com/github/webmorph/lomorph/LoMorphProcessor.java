package com.github.webmorph.lomorph;

import com.github.webmorph.lomorph.annotation.LoApi;
import com.github.webmorph.lomorph.annotation.LoGetter;
import com.github.webmorph.lomorph.annotation.LoIgnore;
import com.github.webmorph.lomorph.annotation.LoSetter;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Annotation processor for {@link LoApi}, {@link LoGetter} and {@link LoSetter}.
 * <p>
 * This processor generates interfaces with getter and/or setter method signatures
 * based on the fields of the annotated class.
 * <p>
 * Supported annotations:
 * <ul>
 *     <li>{@code @LoGetter} — generates an interface with getters</li>
 *     <li>{@code @LoSetter} — generates an interface with setters (excluding final fields)</li>
 *     <li>{@code @LoIgnore} — excludes the field from generation</li>
 *     <li>{@code @LoApi} — generates an interface with getters and setters</li>
 * </ul>
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {
        "com.github.webmorph.lomorph.annotation.LoApi",
        "com.github.webmorph.lomorph.annotation.LoIgnore",
        "com.github.webmorph.lomorph.annotation.LoSetter",
        "com.github.webmorph.lomorph.annotation.LoGetter"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class LoMorphProcessor extends AbstractProcessor {
    /**
     * Template formatter for string substitution: replaces {key} with corresponding value.
     *
     * @param template     Template string containing placeholders like {name}
     * @param placeholders Key-value pairs to substitute
     * @return Formatted string
     */
    public static String format(String template, Map<String, Object> placeholders) {
        if (template == null || placeholders == null || template.isBlank() || placeholders.isEmpty()) return template;
        String result = template;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = String.valueOf(entry.getValue());
            result = result.replace(placeholder, value);
        }
        return result;
    }

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        this.messager = env.getMessager();
    }

    /**
     * Entry point for annotation processing. Scans classes annotated with {@link LoApi}, {@link LoGetter} or {@link LoSetter}
     * and generates interface definitions accordingly.
     *
     * @param annotations Set of annotations detected
     * @param roundEnv    Processing environment
     * @return true if processed
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWithAny(Set.of(LoSetter.class, LoGetter.class, LoApi.class))) {
            if (element.getKind() != ElementKind.CLASS) continue;
            TypeElement classElement = (TypeElement) element;
            String className = classElement.getSimpleName().toString();
            String packageName = processingEnv.getElementUtils().getPackageOf(classElement).toString();
            List<VariableElement> fields = ElementFilter.fieldsIn(classElement.getEnclosedElements()).stream()
                    .filter(field -> {
                        Set<Modifier> modifiers = field.getModifiers();
                        return field.getAnnotation(LoIgnore.class) == null &&
                                !modifiers.contains(Modifier.STATIC) && !modifiers.contains(Modifier.TRANSIENT);
                    })
                    .collect(Collectors.toList());
            if (classElement.getAnnotation(LoSetter.class) != null)
                process("Setter", packageName, className, fields);
            if (classElement.getAnnotation(LoGetter.class) != null)
                process("Getter", packageName, className, fields);
            if (classElement.getAnnotation(LoApi.class) != null)
                process("Api", packageName, className, fields);
        }
        return true;
    }

    private String generateBody(String suffix, boolean isFinal) {
        boolean wantGetter = suffix.equals("Getter") || suffix.equals("Api");
        boolean wantSetter = (suffix.equals("Setter") || suffix.equals("Api")) && !isFinal;
        StringBuilder builder = new StringBuilder();
        if (wantGetter) builder.append("{type} get{capitalizedName}();\n");
        if (wantSetter) builder.append("void set{capitalizedName}({type} {name});\n");
        return builder.toString();
    }

    /**
     * Generates a Java interface with either getter and/or setter method declarations.
     *
     * @param suffix      Either "Getter" or "Setter"
     * @param packageName Package to generate into
     * @param className   Name of the original class
     * @param fields      List of eligible fields
     */
    private void process(String suffix, String packageName, String className, List<VariableElement> fields) {
        try {
            JavaFileObject file = filer.createSourceFile(packageName + "." + className + suffix);
            try (Writer writer = file.openWriter()) {
                String body = fields.stream()
                        .map(field -> {
                            String type = field.asType().toString();
                            String name = field.getSimpleName().toString();
                            return format(generateBody(suffix, field.getModifiers().contains(Modifier.FINAL)), Map.of(
                                    "capitalizedName", name.substring(0, 1).toUpperCase() + name.substring(1),
                                    "type", type,
                                    "name", name
                            ));
                        }).collect(Collectors.joining("\n"));
                writer.write(format("""
                        package {package};
                        
                        public interface {name}{type} {
                        {body}
                        }""", Map.of(
                        "package", packageName,
                        "name", className,
                        "type", suffix,
                        "body", body
                )));
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Generation error: " + e.getMessage());
        }
    }
}
