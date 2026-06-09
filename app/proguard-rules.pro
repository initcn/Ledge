# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# ====================================================================
# TARGETED SERIALIZATION RULES (GSON & ROOM ENTITIES)
# ====================================================================

# Keep generic type signatures and annotations required by Gson reflection
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses

# Target only the fields that Gson actually reads and writes via reflection.
# Instead of keeping entire classes, methods, and constructors (*;),
# this keeps only the fields within your data entities.
-keepclassmembers class app.initcn.ledge.data.entity.** {
    @com.google.gson.annotations.SerializedName <fields>;
    private <fields>;
    protected <fields>;
    public <fields>;
}

# Keep the parameterless constructor for Room/Gson entity instantiation
# without locking down all other class members.
-keepclassmembers class app.initcn.ledge.data.entity.** {
    <init>();
}

# ====================================================================
# SYSTEM & FRAMEWORK REFINEMENTS
# ====================================================================

# Prevent enum name obfuscation strictly for enums matching serialization logic
-keepclassmembers enum app.initcn.ledge.core.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}