package com.google.protobuf;

import com.google.protobuf.ByteString.C0213a;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public abstract class AbstractMessageLite implements MessageLite {

    public static abstract class Builder<BuilderType extends Builder> implements com.google.protobuf.MessageLite.Builder {

        /* renamed from: com.google.protobuf.AbstractMessageLite.Builder.a */
        static final class C0211a extends FilterInputStream {
            private int f834a;

            C0211a(InputStream inputStream, int i) {
                super(inputStream);
                this.f834a = i;
            }

            public int available() throws IOException {
                return Math.min(super.available(), this.f834a);
            }

            public int read() throws IOException {
                if (this.f834a <= 0) {
                    return -1;
                }
                int read = super.read();
                if (read < 0) {
                    return read;
                }
                this.f834a--;
                return read;
            }

            public int read(byte[] bArr, int i, int i2) throws IOException {
                if (this.f834a <= 0) {
                    return -1;
                }
                int read = super.read(bArr, i, Math.min(i2, this.f834a));
                if (read < 0) {
                    return read;
                }
                this.f834a -= read;
                return read;
            }

            public long skip(long j) throws IOException {
                long skip = super.skip(Math.min(j, (long) this.f834a));
                if (skip >= 0) {
                    this.f834a = (int) (((long) this.f834a) - skip);
                }
                return skip;
            }
        }

        public abstract BuilderType clone();

        public abstract BuilderType mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        public BuilderType mergeFrom(CodedInputStream codedInputStream) throws IOException {
            return mergeFrom(codedInputStream, ExtensionRegistryLite.getEmptyRegistry());
        }

        public BuilderType mergeFrom(ByteString byteString) throws InvalidProtocolBufferException {
            try {
                CodedInputStream newCodedInput = byteString.newCodedInput();
                mergeFrom(newCodedInput);
                newCodedInput.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (Throwable e2) {
                throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", e2);
            }
        }

        public BuilderType mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            try {
                CodedInputStream newCodedInput = byteString.newCodedInput();
                mergeFrom(newCodedInput, extensionRegistryLite);
                newCodedInput.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (Throwable e2) {
                throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", e2);
            }
        }

        public BuilderType mergeFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return mergeFrom(bArr, 0, bArr.length);
        }

        public BuilderType mergeFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException {
            try {
                CodedInputStream newInstance = CodedInputStream.newInstance(bArr, i, i2);
                mergeFrom(newInstance);
                newInstance.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (Throwable e2) {
                throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
            }
        }

        public BuilderType mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return mergeFrom(bArr, 0, bArr.length, extensionRegistryLite);
        }

        public BuilderType mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            try {
                CodedInputStream newInstance = CodedInputStream.newInstance(bArr, i, i2);
                mergeFrom(newInstance, extensionRegistryLite);
                newInstance.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (Throwable e2) {
                throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
            }
        }

        public BuilderType mergeFrom(InputStream inputStream) throws IOException {
            CodedInputStream newInstance = CodedInputStream.newInstance(inputStream);
            mergeFrom(newInstance);
            newInstance.checkLastTagWas(0);
            return this;
        }

        public BuilderType mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            CodedInputStream newInstance = CodedInputStream.newInstance(inputStream);
            mergeFrom(newInstance, extensionRegistryLite);
            newInstance.checkLastTagWas(0);
            return this;
        }

        public boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            int read = inputStream.read();
            if (read == -1) {
                return false;
            }
            mergeFrom(new C0211a(inputStream, CodedInputStream.readRawVarint32(read, inputStream)), extensionRegistryLite);
            return true;
        }

        public boolean mergeDelimitedFrom(InputStream inputStream) throws IOException {
            return mergeDelimitedFrom(inputStream, ExtensionRegistryLite.getEmptyRegistry());
        }

        protected static UninitializedMessageException newUninitializedMessageException(MessageLite messageLite) {
            return new UninitializedMessageException(messageLite);
        }

        protected static <T> void addAll(Iterable<T> iterable, Collection<? super T> collection) {
            for (T t : iterable) {
                if (t == null) {
                    throw new NullPointerException();
                }
            }
            if (iterable instanceof Collection) {
                collection.addAll((Collection) iterable);
                return;
            }
            for (T t2 : iterable) {
                collection.add(t2);
            }
        }
    }

    public ByteString toByteString() {
        try {
            C0213a newCodedBuilder = ByteString.newCodedBuilder(getSerializedSize());
            writeTo(newCodedBuilder.m923b());
            return newCodedBuilder.m922a();
        } catch (Throwable e) {
            throw new RuntimeException("Serializing to a ByteString threw an IOException (should never happen).", e);
        }
    }

    public byte[] toByteArray() {
        try {
            byte[] bArr = new byte[getSerializedSize()];
            CodedOutputStream newInstance = CodedOutputStream.newInstance(bArr);
            writeTo(newInstance);
            newInstance.checkNoSpaceLeft();
            return bArr;
        } catch (Throwable e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        CodedOutputStream newInstance = CodedOutputStream.newInstance(outputStream, CodedOutputStream.computePreferredBufferSize(getSerializedSize()));
        writeTo(newInstance);
        newInstance.flush();
    }

    public void writeDelimitedTo(OutputStream outputStream) throws IOException {
        int serializedSize = getSerializedSize();
        CodedOutputStream newInstance = CodedOutputStream.newInstance(outputStream, CodedOutputStream.computePreferredBufferSize(CodedOutputStream.computeRawVarint32Size(serializedSize) + serializedSize));
        newInstance.writeRawVarint32(serializedSize);
        writeTo(newInstance);
        newInstance.flush();
    }
}
