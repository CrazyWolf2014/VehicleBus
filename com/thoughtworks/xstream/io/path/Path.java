package com.thoughtworks.xstream.io.path;

import com.thoughtworks.xstream.core.util.FastStack;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private static final Path DOT;
    private final String[] chunks;
    private transient String pathAsString;
    private transient String pathExplicit;

    static {
        DOT = new Path(new String[]{"."});
    }

    public Path(String pathAsString) {
        List result = new ArrayList();
        int currentIndex = 0;
        this.pathAsString = pathAsString;
        while (true) {
            int nextSeparator = pathAsString.indexOf(47, currentIndex);
            if (nextSeparator != -1) {
                result.add(normalize(pathAsString, currentIndex, nextSeparator));
                currentIndex = nextSeparator + 1;
            } else {
                result.add(normalize(pathAsString, currentIndex, pathAsString.length()));
                String[] arr = new String[result.size()];
                result.toArray(arr);
                this.chunks = arr;
                return;
            }
        }
    }

    private String normalize(String s, int start, int end) {
        if (end - start <= 3 || s.charAt(end - 3) != '[' || s.charAt(end - 2) != '1' || s.charAt(end - 1) != ']') {
            return s.substring(start, end);
        }
        this.pathAsString = null;
        return s.substring(start, end - 3);
    }

    public Path(String[] chunks) {
        this.chunks = chunks;
    }

    public String toString() {
        if (this.pathAsString == null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < this.chunks.length; i++) {
                if (i > 0) {
                    buffer.append('/');
                }
                buffer.append(this.chunks[i]);
            }
            this.pathAsString = buffer.toString();
        }
        return this.pathAsString;
    }

    public String explicit() {
        if (this.pathExplicit == null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < this.chunks.length; i++) {
                if (i > 0) {
                    buffer.append('/');
                }
                String chunk = this.chunks[i];
                buffer.append(chunk);
                int length = chunk.length();
                if (length > 0) {
                    char c = chunk.charAt(length - 1);
                    if (!(c == ']' || c == '.')) {
                        buffer.append("[1]");
                    }
                }
            }
            this.pathExplicit = buffer.toString();
        }
        return this.pathExplicit;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Path)) {
            return false;
        }
        Path other = (Path) o;
        if (this.chunks.length != other.chunks.length) {
            return false;
        }
        for (int i = 0; i < this.chunks.length; i++) {
            if (!this.chunks[i].equals(other.chunks[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 543645643;
        for (String hashCode : this.chunks) {
            result = (result * 29) + hashCode.hashCode();
        }
        return result;
    }

    public Path relativeTo(Path that) {
        int depthOfPathDivergence = depthOfPathDivergence(this.chunks, that.chunks);
        String[] result = new String[((this.chunks.length + that.chunks.length) - (depthOfPathDivergence * 2))];
        int count = 0;
        int i = depthOfPathDivergence;
        while (i < this.chunks.length) {
            int count2 = count + 1;
            result[count] = "..";
            i++;
            count = count2;
        }
        int j = depthOfPathDivergence;
        while (j < that.chunks.length) {
            count2 = count + 1;
            result[count] = that.chunks[j];
            j++;
            count = count2;
        }
        if (count == 0) {
            return DOT;
        }
        return new Path(result);
    }

    private int depthOfPathDivergence(String[] path1, String[] path2) {
        int minLength = Math.min(path1.length, path2.length);
        for (int i = 0; i < minLength; i++) {
            if (!path1[i].equals(path2[i])) {
                return i;
            }
        }
        return minLength;
    }

    public Path apply(Path relativePath) {
        int i;
        FastStack absoluteStack = new FastStack(16);
        for (Object push : this.chunks) {
            absoluteStack.push(push);
        }
        for (String relativeChunk : relativePath.chunks) {
            if (relativeChunk.equals("..")) {
                absoluteStack.pop();
            } else if (!relativeChunk.equals(".")) {
                absoluteStack.push(relativeChunk);
            }
        }
        String[] result = new String[absoluteStack.size()];
        for (i = 0; i < result.length; i++) {
            result[i] = (String) absoluteStack.get(i);
        }
        return new Path(result);
    }

    public boolean isAncestor(Path child) {
        if (child == null || child.chunks.length < this.chunks.length) {
            return false;
        }
        for (int i = 0; i < this.chunks.length; i++) {
            if (!this.chunks[i].equals(child.chunks[i])) {
                return false;
            }
        }
        return true;
    }
}
