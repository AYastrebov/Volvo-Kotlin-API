# kotlinx-serialization: keep @Serializable classes and their generated serializers
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.github.ayastrebov.volvo.api.model.**$$serializer { *; }
-keepclassmembers class com.github.ayastrebov.volvo.api.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.github.ayastrebov.volvo.api.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep exception classes (used in catch blocks by name)
-keep public class com.github.ayastrebov.volvo.api.exception.** { *; }
