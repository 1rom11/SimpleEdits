package net.romit.simpleedits.util;

import java.util.Objects;

public class Identifier {
    private final String namespace;
    private final String path;

    public static final String DEFAULT_NAMESPACE = "minecraft";

    public Identifier(String namespace, String path) {
        if (!isValidNamespace(namespace)) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        if (!isValidPath(path)) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
        this.namespace = namespace;
        this.path = path;
    }

    public Identifier(String identifier) {
        String[] parts = identifier.split(":", 2);
        if (parts.length == 2) {
            this.namespace = parts[0];
            this.path = parts[1];
        } else if (parts.length == 1) {
            this.namespace = DEFAULT_NAMESPACE;
            this.path = parts[0];
        } else {
            throw new IllegalArgumentException("Invalid identifier format: " + identifier);
        }
        if (!isValidNamespace(this.namespace)) {
            throw new IllegalArgumentException("Invalid namespace: " + this.namespace);
        }
        if (!isValidPath(this.path)) {
            throw new IllegalArgumentException("Invalid path: " + this.path);
        }
    }

    /**
     * Static factory method to create an Identifier from a string.
     *
     * @param blockId the string in the format "namespace:path"
     * @return an Identifier instance
     * @throws IllegalArgumentException if the string is invalid
     */
    public static net.minecraft.util.Identifier of(String blockId) {
        if (blockId == null || blockId.isEmpty()) {
            throw new IllegalArgumentException("Block ID cannot be null or empty.");
        }

        String[] parts = blockId.split(":", 2);
        if (parts.length == 1) {
            return new Identifier(DEFAULT_NAMESPACE, parts[0]);
        } else if (parts.length == 2) {
            return new Identifier(parts[0], parts[1]);
        } else {
            throw new IllegalArgumentException("Invalid format for block ID: " + blockId);
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return namespace.equals(that.namespace) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    public static boolean isValidNamespace(String namespace) {
        return namespace != null && namespace.matches("[a-z0-9_.-]+");
    }

    public static boolean isValidPath(String path) {
        return path != null && path.matches("[a-z0-9_/.-]+");
    }
}
