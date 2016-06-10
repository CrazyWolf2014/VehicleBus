package com.amap.mapapi.map;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;
import com.google.protobuf.Internal;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.RepeatedFieldBuilder;
import com.google.protobuf.UnknownFieldSet;
import com.ifoer.expeditionphone.WelcomeActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.kxml2.wap.WbxmlParser;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public final class TrafficProtos {
    private static FileDescriptor descriptor;
    private static Descriptor internal_static_traffic_TrafficTile_TrafficIncident_descriptor;
    private static FieldAccessorTable f509x10786b62;
    private static Descriptor internal_static_traffic_TrafficTile_TrafficSegment_descriptor;
    private static FieldAccessorTable f510xbb770691;
    private static Descriptor internal_static_traffic_TrafficTile_descriptor;
    private static FieldAccessorTable internal_static_traffic_TrafficTile_fieldAccessorTable;

    public interface TrafficTileOrBuilder extends MessageOrBuilder {
        TrafficIncident getTrafficIncident(int i);

        int getTrafficIncidentCount();

        List<TrafficIncident> getTrafficIncidentList();

        TrafficIncidentOrBuilder getTrafficIncidentOrBuilder(int i);

        List<? extends TrafficIncidentOrBuilder> getTrafficIncidentOrBuilderList();

        TrafficSegment getTrafficSegment(int i);

        int getTrafficSegmentCount();

        List<TrafficSegment> getTrafficSegmentList();

        TrafficSegmentOrBuilder getTrafficSegmentOrBuilder(int i);

        List<? extends TrafficSegmentOrBuilder> getTrafficSegmentOrBuilderList();

        ByteString getVertices();

        boolean hasVertices();
    }

    public static final class TrafficTile extends GeneratedMessage implements TrafficTileOrBuilder {
        public static final int TRAFFICINCIDENT_FIELD_NUMBER = 7;
        public static final int TRAFFICSEGMENT_FIELD_NUMBER = 2;
        public static final int VERTICES_FIELD_NUMBER = 1;
        private static final TrafficTile defaultInstance;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<TrafficIncident> trafficIncident_;
        private List<TrafficSegment> trafficSegment_;
        private ByteString vertices_;

        public interface TrafficIncidentOrBuilder extends MessageOrBuilder {
            String getDescription();

            double getEndTime();

            ByteString getIncidentVertex();

            double getLastUpdated();

            String getLocation();

            double getStartTime();

            String getTitle();

            Type getType();

            long getUID();

            int getVertexOffset();

            boolean hasDescription();

            boolean hasEndTime();

            boolean hasIncidentVertex();

            boolean hasLastUpdated();

            boolean hasLocation();

            boolean hasStartTime();

            boolean hasTitle();

            boolean hasType();

            boolean hasUID();

            boolean hasVertexOffset();
        }

        public interface TrafficSegmentOrBuilder extends MessageOrBuilder {
            TrafficSpeed getSpeed();

            int getVertexCount();

            int getVertexOffset();

            int getWidth();

            boolean hasSpeed();

            boolean hasVertexCount();

            boolean hasVertexOffset();

            boolean hasWidth();
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements TrafficTileOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<TrafficIncident, Builder, TrafficIncidentOrBuilder> trafficIncidentBuilder_;
            private List<TrafficIncident> trafficIncident_;
            private RepeatedFieldBuilder<TrafficSegment, Builder, TrafficSegmentOrBuilder> trafficSegmentBuilder_;
            private List<TrafficSegment> trafficSegment_;
            private ByteString vertices_;

            public static final Descriptor getDescriptor() {
                return TrafficProtos.internal_static_traffic_TrafficTile_descriptor;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return TrafficProtos.internal_static_traffic_TrafficTile_fieldAccessorTable;
            }

            private Builder() {
                this.vertices_ = ByteString.EMPTY;
                this.trafficSegment_ = Collections.emptyList();
                this.trafficIncident_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.vertices_ = ByteString.EMPTY;
                this.trafficSegment_ = Collections.emptyList();
                this.trafficIncident_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (TrafficTile.alwaysUseFieldBuilders) {
                    getTrafficSegmentFieldBuilder();
                    getTrafficIncidentFieldBuilder();
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.vertices_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                if (this.trafficSegmentBuilder_ == null) {
                    this.trafficSegment_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.trafficSegmentBuilder_.clear();
                }
                if (this.trafficIncidentBuilder_ == null) {
                    this.trafficIncident_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    this.trafficIncidentBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public Descriptor getDescriptorForType() {
                return TrafficTile.getDescriptor();
            }

            public TrafficTile getDefaultInstanceForType() {
                return TrafficTile.getDefaultInstance();
            }

            public TrafficTile build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            private TrafficTile buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public TrafficTile buildPartial() {
                int i = TrafficTile.VERTICES_FIELD_NUMBER;
                TrafficTile trafficTile = new TrafficTile();
                if ((this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) != TrafficTile.VERTICES_FIELD_NUMBER) {
                    i = 0;
                }
                trafficTile.vertices_ = this.vertices_;
                if (this.trafficSegmentBuilder_ == null) {
                    if ((this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                        this.trafficSegment_ = Collections.unmodifiableList(this.trafficSegment_);
                        this.bitField0_ &= -3;
                    }
                    trafficTile.trafficSegment_ = this.trafficSegment_;
                } else {
                    trafficTile.trafficSegment_ = this.trafficSegmentBuilder_.build();
                }
                if (this.trafficIncidentBuilder_ == null) {
                    if ((this.bitField0_ & 4) == 4) {
                        this.trafficIncident_ = Collections.unmodifiableList(this.trafficIncident_);
                        this.bitField0_ &= -5;
                    }
                    trafficTile.trafficIncident_ = this.trafficIncident_;
                } else {
                    trafficTile.trafficIncident_ = this.trafficIncidentBuilder_.build();
                }
                trafficTile.bitField0_ = i;
                onBuilt();
                return trafficTile;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof TrafficTile) {
                    return mergeFrom((TrafficTile) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(TrafficTile trafficTile) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (trafficTile != TrafficTile.getDefaultInstance()) {
                    if (trafficTile.hasVertices()) {
                        setVertices(trafficTile.getVertices());
                    }
                    if (this.trafficSegmentBuilder_ == null) {
                        if (!trafficTile.trafficSegment_.isEmpty()) {
                            if (this.trafficSegment_.isEmpty()) {
                                this.trafficSegment_ = trafficTile.trafficSegment_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureTrafficSegmentIsMutable();
                                this.trafficSegment_.addAll(trafficTile.trafficSegment_);
                            }
                            onChanged();
                        }
                    } else if (!trafficTile.trafficSegment_.isEmpty()) {
                        if (this.trafficSegmentBuilder_.isEmpty()) {
                            this.trafficSegmentBuilder_.dispose();
                            this.trafficSegmentBuilder_ = null;
                            this.trafficSegment_ = trafficTile.trafficSegment_;
                            this.bitField0_ &= -3;
                            this.trafficSegmentBuilder_ = TrafficTile.alwaysUseFieldBuilders ? getTrafficSegmentFieldBuilder() : null;
                        } else {
                            this.trafficSegmentBuilder_.addAllMessages(trafficTile.trafficSegment_);
                        }
                    }
                    if (this.trafficIncidentBuilder_ == null) {
                        if (!trafficTile.trafficIncident_.isEmpty()) {
                            if (this.trafficIncident_.isEmpty()) {
                                this.trafficIncident_ = trafficTile.trafficIncident_;
                                this.bitField0_ &= -5;
                            } else {
                                ensureTrafficIncidentIsMutable();
                                this.trafficIncident_.addAll(trafficTile.trafficIncident_);
                            }
                            onChanged();
                        }
                    } else if (!trafficTile.trafficIncident_.isEmpty()) {
                        if (this.trafficIncidentBuilder_.isEmpty()) {
                            this.trafficIncidentBuilder_.dispose();
                            this.trafficIncidentBuilder_ = null;
                            this.trafficIncident_ = trafficTile.trafficIncident_;
                            this.bitField0_ &= -5;
                            if (TrafficTile.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getTrafficIncidentFieldBuilder();
                            }
                            this.trafficIncidentBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.trafficIncidentBuilder_.addAllMessages(trafficTile.trafficIncident_);
                        }
                    }
                    mergeUnknownFields(trafficTile.getUnknownFields());
                }
                return this;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getTrafficSegmentCount(); i += TrafficTile.VERTICES_FIELD_NUMBER) {
                    if (!getTrafficSegment(i).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    Object newBuilder2;
                    switch (readTag) {
                        case KEYRecord.OWNER_USER /*0*/:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                            this.bitField0_ |= TrafficTile.VERTICES_FIELD_NUMBER;
                            this.vertices_ = codedInputStream.readBytes();
                            continue;
                        case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                            newBuilder2 = TrafficSegment.newBuilder();
                            codedInputStream.readGroup(TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER, newBuilder2, extensionRegistryLite);
                            addTrafficSegment(newBuilder2.buildPartial());
                            continue;
                        case 59:
                            newBuilder2 = TrafficIncident.newBuilder();
                            codedInputStream.readGroup(TrafficTile.TRAFFICINCIDENT_FIELD_NUMBER, newBuilder2, extensionRegistryLite);
                            addTrafficIncident(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public boolean hasVertices() {
                return (this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER;
            }

            public ByteString getVertices() {
                return this.vertices_;
            }

            public Builder setVertices(ByteString byteString) {
                if (byteString == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= TrafficTile.VERTICES_FIELD_NUMBER;
                this.vertices_ = byteString;
                onChanged();
                return this;
            }

            public Builder clearVertices() {
                this.bitField0_ &= -2;
                this.vertices_ = TrafficTile.getDefaultInstance().getVertices();
                onChanged();
                return this;
            }

            private void ensureTrafficSegmentIsMutable() {
                if ((this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) != TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                    this.trafficSegment_ = new ArrayList(this.trafficSegment_);
                    this.bitField0_ |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                }
            }

            public List<TrafficSegment> getTrafficSegmentList() {
                if (this.trafficSegmentBuilder_ == null) {
                    return Collections.unmodifiableList(this.trafficSegment_);
                }
                return this.trafficSegmentBuilder_.getMessageList();
            }

            public int getTrafficSegmentCount() {
                if (this.trafficSegmentBuilder_ == null) {
                    return this.trafficSegment_.size();
                }
                return this.trafficSegmentBuilder_.getCount();
            }

            public TrafficSegment getTrafficSegment(int i) {
                if (this.trafficSegmentBuilder_ == null) {
                    return (TrafficSegment) this.trafficSegment_.get(i);
                }
                return (TrafficSegment) this.trafficSegmentBuilder_.getMessage(i);
            }

            public Builder setTrafficSegment(int i, TrafficSegment trafficSegment) {
                if (this.trafficSegmentBuilder_ != null) {
                    this.trafficSegmentBuilder_.setMessage(i, trafficSegment);
                } else if (trafficSegment == null) {
                    throw new NullPointerException();
                } else {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.set(i, trafficSegment);
                    onChanged();
                }
                return this;
            }

            public Builder setTrafficSegment(int i, Builder builder) {
                if (this.trafficSegmentBuilder_ == null) {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.set(i, builder.build());
                    onChanged();
                } else {
                    this.trafficSegmentBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder addTrafficSegment(TrafficSegment trafficSegment) {
                if (this.trafficSegmentBuilder_ != null) {
                    this.trafficSegmentBuilder_.addMessage(trafficSegment);
                } else if (trafficSegment == null) {
                    throw new NullPointerException();
                } else {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.add(trafficSegment);
                    onChanged();
                }
                return this;
            }

            public Builder addTrafficSegment(int i, TrafficSegment trafficSegment) {
                if (this.trafficSegmentBuilder_ != null) {
                    this.trafficSegmentBuilder_.addMessage(i, trafficSegment);
                } else if (trafficSegment == null) {
                    throw new NullPointerException();
                } else {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.add(i, trafficSegment);
                    onChanged();
                }
                return this;
            }

            public Builder addTrafficSegment(Builder builder) {
                if (this.trafficSegmentBuilder_ == null) {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.add(builder.build());
                    onChanged();
                } else {
                    this.trafficSegmentBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addTrafficSegment(int i, Builder builder) {
                if (this.trafficSegmentBuilder_ == null) {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.add(i, builder.build());
                    onChanged();
                } else {
                    this.trafficSegmentBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addAllTrafficSegment(Iterable<? extends TrafficSegment> iterable) {
                if (this.trafficSegmentBuilder_ == null) {
                    ensureTrafficSegmentIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.trafficSegment_);
                    onChanged();
                } else {
                    this.trafficSegmentBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder clearTrafficSegment() {
                if (this.trafficSegmentBuilder_ == null) {
                    this.trafficSegment_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.trafficSegmentBuilder_.clear();
                }
                return this;
            }

            public Builder removeTrafficSegment(int i) {
                if (this.trafficSegmentBuilder_ == null) {
                    ensureTrafficSegmentIsMutable();
                    this.trafficSegment_.remove(i);
                    onChanged();
                } else {
                    this.trafficSegmentBuilder_.remove(i);
                }
                return this;
            }

            public Builder getTrafficSegmentBuilder(int i) {
                return (Builder) getTrafficSegmentFieldBuilder().getBuilder(i);
            }

            public TrafficSegmentOrBuilder getTrafficSegmentOrBuilder(int i) {
                if (this.trafficSegmentBuilder_ == null) {
                    return (TrafficSegmentOrBuilder) this.trafficSegment_.get(i);
                }
                return (TrafficSegmentOrBuilder) this.trafficSegmentBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends TrafficSegmentOrBuilder> getTrafficSegmentOrBuilderList() {
                if (this.trafficSegmentBuilder_ != null) {
                    return this.trafficSegmentBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.trafficSegment_);
            }

            public Builder addTrafficSegmentBuilder() {
                return (Builder) getTrafficSegmentFieldBuilder().addBuilder(TrafficSegment.getDefaultInstance());
            }

            public Builder addTrafficSegmentBuilder(int i) {
                return (Builder) getTrafficSegmentFieldBuilder().addBuilder(i, TrafficSegment.getDefaultInstance());
            }

            public List<Builder> getTrafficSegmentBuilderList() {
                return getTrafficSegmentFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilder<TrafficSegment, Builder, TrafficSegmentOrBuilder> getTrafficSegmentFieldBuilder() {
                if (this.trafficSegmentBuilder_ == null) {
                    this.trafficSegmentBuilder_ = new RepeatedFieldBuilder(this.trafficSegment_, (this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER, getParentForChildren(), isClean());
                    this.trafficSegment_ = null;
                }
                return this.trafficSegmentBuilder_;
            }

            private void ensureTrafficIncidentIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.trafficIncident_ = new ArrayList(this.trafficIncident_);
                    this.bitField0_ |= 4;
                }
            }

            public List<TrafficIncident> getTrafficIncidentList() {
                if (this.trafficIncidentBuilder_ == null) {
                    return Collections.unmodifiableList(this.trafficIncident_);
                }
                return this.trafficIncidentBuilder_.getMessageList();
            }

            public int getTrafficIncidentCount() {
                if (this.trafficIncidentBuilder_ == null) {
                    return this.trafficIncident_.size();
                }
                return this.trafficIncidentBuilder_.getCount();
            }

            public TrafficIncident getTrafficIncident(int i) {
                if (this.trafficIncidentBuilder_ == null) {
                    return (TrafficIncident) this.trafficIncident_.get(i);
                }
                return (TrafficIncident) this.trafficIncidentBuilder_.getMessage(i);
            }

            public Builder setTrafficIncident(int i, TrafficIncident trafficIncident) {
                if (this.trafficIncidentBuilder_ != null) {
                    this.trafficIncidentBuilder_.setMessage(i, trafficIncident);
                } else if (trafficIncident == null) {
                    throw new NullPointerException();
                } else {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.set(i, trafficIncident);
                    onChanged();
                }
                return this;
            }

            public Builder setTrafficIncident(int i, Builder builder) {
                if (this.trafficIncidentBuilder_ == null) {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.set(i, builder.build());
                    onChanged();
                } else {
                    this.trafficIncidentBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder addTrafficIncident(TrafficIncident trafficIncident) {
                if (this.trafficIncidentBuilder_ != null) {
                    this.trafficIncidentBuilder_.addMessage(trafficIncident);
                } else if (trafficIncident == null) {
                    throw new NullPointerException();
                } else {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.add(trafficIncident);
                    onChanged();
                }
                return this;
            }

            public Builder addTrafficIncident(int i, TrafficIncident trafficIncident) {
                if (this.trafficIncidentBuilder_ != null) {
                    this.trafficIncidentBuilder_.addMessage(i, trafficIncident);
                } else if (trafficIncident == null) {
                    throw new NullPointerException();
                } else {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.add(i, trafficIncident);
                    onChanged();
                }
                return this;
            }

            public Builder addTrafficIncident(Builder builder) {
                if (this.trafficIncidentBuilder_ == null) {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.add(builder.build());
                    onChanged();
                } else {
                    this.trafficIncidentBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addTrafficIncident(int i, Builder builder) {
                if (this.trafficIncidentBuilder_ == null) {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.add(i, builder.build());
                    onChanged();
                } else {
                    this.trafficIncidentBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addAllTrafficIncident(Iterable<? extends TrafficIncident> iterable) {
                if (this.trafficIncidentBuilder_ == null) {
                    ensureTrafficIncidentIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.trafficIncident_);
                    onChanged();
                } else {
                    this.trafficIncidentBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder clearTrafficIncident() {
                if (this.trafficIncidentBuilder_ == null) {
                    this.trafficIncident_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    onChanged();
                } else {
                    this.trafficIncidentBuilder_.clear();
                }
                return this;
            }

            public Builder removeTrafficIncident(int i) {
                if (this.trafficIncidentBuilder_ == null) {
                    ensureTrafficIncidentIsMutable();
                    this.trafficIncident_.remove(i);
                    onChanged();
                } else {
                    this.trafficIncidentBuilder_.remove(i);
                }
                return this;
            }

            public Builder getTrafficIncidentBuilder(int i) {
                return (Builder) getTrafficIncidentFieldBuilder().getBuilder(i);
            }

            public TrafficIncidentOrBuilder getTrafficIncidentOrBuilder(int i) {
                if (this.trafficIncidentBuilder_ == null) {
                    return (TrafficIncidentOrBuilder) this.trafficIncident_.get(i);
                }
                return (TrafficIncidentOrBuilder) this.trafficIncidentBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends TrafficIncidentOrBuilder> getTrafficIncidentOrBuilderList() {
                if (this.trafficIncidentBuilder_ != null) {
                    return this.trafficIncidentBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.trafficIncident_);
            }

            public Builder addTrafficIncidentBuilder() {
                return (Builder) getTrafficIncidentFieldBuilder().addBuilder(TrafficIncident.getDefaultInstance());
            }

            public Builder addTrafficIncidentBuilder(int i) {
                return (Builder) getTrafficIncidentFieldBuilder().addBuilder(i, TrafficIncident.getDefaultInstance());
            }

            public List<Builder> getTrafficIncidentBuilderList() {
                return getTrafficIncidentFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilder<TrafficIncident, Builder, TrafficIncidentOrBuilder> getTrafficIncidentFieldBuilder() {
                if (this.trafficIncidentBuilder_ == null) {
                    this.trafficIncidentBuilder_ = new RepeatedFieldBuilder(this.trafficIncident_, (this.bitField0_ & 4) == 4, getParentForChildren(), isClean());
                    this.trafficIncident_ = null;
                }
                return this.trafficIncidentBuilder_;
            }
        }

        public static final class TrafficIncident extends GeneratedMessage implements TrafficIncidentOrBuilder {
            public static final int DESCRIPTION_FIELD_NUMBER = 10;
            public static final int ENDTIME_FIELD_NUMBER = 15;
            public static final int INCIDENTVERTEX_FIELD_NUMBER = 13;
            public static final int LASTUPDATED_FIELD_NUMBER = 16;
            public static final int LOCATION_FIELD_NUMBER = 11;
            public static final int STARTTIME_FIELD_NUMBER = 14;
            public static final int TITLE_FIELD_NUMBER = 9;
            public static final int TYPE_FIELD_NUMBER = 17;
            public static final int UID_FIELD_NUMBER = 8;
            public static final int VERTEXOFFSET_FIELD_NUMBER = 12;
            private static final TrafficIncident defaultInstance;
            private static final long serialVersionUID = 0;
            private int bitField0_;
            private Object description_;
            private double endTime_;
            private ByteString incidentVertex_;
            private double lastUpdated_;
            private Object location_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private double startTime_;
            private Object title_;
            private Type type_;
            private long uID_;
            private int vertexOffset_;

            public enum Type implements ProtocolMessageEnum {
                CONSTRUCTION_LONG_TERM(0, CONSTRUCTION_LONG_TERM_VALUE),
                CONSTRUCTION_SHORT_TERM(CONSTRUCTION_LONG_TERM_VALUE, CONSTRUCTION_SHORT_TERM_VALUE),
                ROAD_CLOSURE(CONSTRUCTION_SHORT_TERM_VALUE, ROAD_CLOSURE_VALUE),
                LANE_CLOSURE(ROAD_CLOSURE_VALUE, LANE_CLOSURE_VALUE),
                VEHICLE(LANE_CLOSURE_VALUE, VEHICLE_VALUE),
                DEBRIS(VEHICLE_VALUE, DEBRIS_VALUE),
                WEATHER(DEBRIS_VALUE, WEATHER_VALUE),
                EVENT(WEATHER_VALUE, EVENT_VALUE);
                
                public static final int CONSTRUCTION_LONG_TERM_VALUE = 1;
                public static final int CONSTRUCTION_SHORT_TERM_VALUE = 2;
                public static final int DEBRIS_VALUE = 6;
                public static final int EVENT_VALUE = 8;
                public static final int LANE_CLOSURE_VALUE = 4;
                public static final int ROAD_CLOSURE_VALUE = 3;
                private static final Type[] VALUES;
                public static final int VEHICLE_VALUE = 5;
                public static final int WEATHER_VALUE = 7;
                private static EnumLiteMap<Type> internalValueMap;
                private final int index;
                private final int value;

                static {
                    internalValueMap = new az();
                    Type[] typeArr = new Type[EVENT_VALUE];
                    typeArr[0] = CONSTRUCTION_LONG_TERM;
                    typeArr[CONSTRUCTION_LONG_TERM_VALUE] = CONSTRUCTION_SHORT_TERM;
                    typeArr[CONSTRUCTION_SHORT_TERM_VALUE] = ROAD_CLOSURE;
                    typeArr[ROAD_CLOSURE_VALUE] = LANE_CLOSURE;
                    typeArr[LANE_CLOSURE_VALUE] = VEHICLE;
                    typeArr[VEHICLE_VALUE] = DEBRIS;
                    typeArr[DEBRIS_VALUE] = WEATHER;
                    typeArr[WEATHER_VALUE] = EVENT;
                    VALUES = typeArr;
                }

                public final int getNumber() {
                    return this.value;
                }

                public static Type valueOf(int i) {
                    switch (i) {
                        case CONSTRUCTION_LONG_TERM_VALUE:
                            return CONSTRUCTION_LONG_TERM;
                        case CONSTRUCTION_SHORT_TERM_VALUE:
                            return CONSTRUCTION_SHORT_TERM;
                        case ROAD_CLOSURE_VALUE:
                            return ROAD_CLOSURE;
                        case LANE_CLOSURE_VALUE:
                            return LANE_CLOSURE;
                        case VEHICLE_VALUE:
                            return VEHICLE;
                        case DEBRIS_VALUE:
                            return DEBRIS;
                        case WEATHER_VALUE:
                            return WEATHER;
                        case EVENT_VALUE:
                            return EVENT;
                        default:
                            return null;
                    }
                }

                public static EnumLiteMap<Type> internalGetValueMap() {
                    return internalValueMap;
                }

                public final EnumValueDescriptor getValueDescriptor() {
                    return (EnumValueDescriptor) getDescriptor().getValues().get(this.index);
                }

                public final EnumDescriptor getDescriptorForType() {
                    return getDescriptor();
                }

                public static final EnumDescriptor getDescriptor() {
                    return (EnumDescriptor) TrafficIncident.getDescriptor().getEnumTypes().get(0);
                }

                public static Type valueOf(EnumValueDescriptor enumValueDescriptor) {
                    if (enumValueDescriptor.getType() == getDescriptor()) {
                        return VALUES[enumValueDescriptor.getIndex()];
                    }
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }

                private Type(int i, int i2) {
                    this.index = i;
                    this.value = i2;
                }
            }

            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements TrafficIncidentOrBuilder {
                private int bitField0_;
                private Object description_;
                private double endTime_;
                private ByteString incidentVertex_;
                private double lastUpdated_;
                private Object location_;
                private double startTime_;
                private Object title_;
                private Type type_;
                private long uID_;
                private int vertexOffset_;

                public static final Descriptor getDescriptor() {
                    return TrafficProtos.internal_static_traffic_TrafficTile_TrafficIncident_descriptor;
                }

                protected FieldAccessorTable internalGetFieldAccessorTable() {
                    return TrafficProtos.f509x10786b62;
                }

                private Builder() {
                    this.title_ = XmlPullParser.NO_NAMESPACE;
                    this.description_ = XmlPullParser.NO_NAMESPACE;
                    this.location_ = XmlPullParser.NO_NAMESPACE;
                    this.incidentVertex_ = ByteString.EMPTY;
                    this.type_ = Type.CONSTRUCTION_LONG_TERM;
                    maybeForceBuilderInitialization();
                }

                private Builder(BuilderParent builderParent) {
                    super(builderParent);
                    this.title_ = XmlPullParser.NO_NAMESPACE;
                    this.description_ = XmlPullParser.NO_NAMESPACE;
                    this.location_ = XmlPullParser.NO_NAMESPACE;
                    this.incidentVertex_ = ByteString.EMPTY;
                    this.type_ = Type.CONSTRUCTION_LONG_TERM;
                    maybeForceBuilderInitialization();
                }

                private void maybeForceBuilderInitialization() {
                    if (!TrafficIncident.alwaysUseFieldBuilders) {
                    }
                }

                private static Builder create() {
                    return new Builder();
                }

                public Builder clear() {
                    super.clear();
                    this.uID_ = 0;
                    this.bitField0_ &= -2;
                    this.title_ = XmlPullParser.NO_NAMESPACE;
                    this.bitField0_ &= -3;
                    this.description_ = XmlPullParser.NO_NAMESPACE;
                    this.bitField0_ &= -5;
                    this.location_ = XmlPullParser.NO_NAMESPACE;
                    this.bitField0_ &= -9;
                    this.vertexOffset_ = 0;
                    this.bitField0_ &= -17;
                    this.incidentVertex_ = ByteString.EMPTY;
                    this.bitField0_ &= -33;
                    this.startTime_ = 0.0d;
                    this.bitField0_ &= -65;
                    this.endTime_ = 0.0d;
                    this.bitField0_ &= -129;
                    this.lastUpdated_ = 0.0d;
                    this.bitField0_ &= -257;
                    this.type_ = Type.CONSTRUCTION_LONG_TERM;
                    this.bitField0_ &= -513;
                    return this;
                }

                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                public Descriptor getDescriptorForType() {
                    return TrafficIncident.getDescriptor();
                }

                public TrafficIncident getDefaultInstanceForType() {
                    return TrafficIncident.getDefaultInstance();
                }

                public TrafficIncident build() {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
                }

                private TrafficIncident buildParsed() throws InvalidProtocolBufferException {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
                }

                public TrafficIncident buildPartial() {
                    int i = TrafficTile.VERTICES_FIELD_NUMBER;
                    TrafficIncident trafficIncident = new TrafficIncident();
                    int i2 = this.bitField0_;
                    if ((i2 & TrafficTile.VERTICES_FIELD_NUMBER) != TrafficTile.VERTICES_FIELD_NUMBER) {
                        i = 0;
                    }
                    trafficIncident.uID_ = this.uID_;
                    if ((i2 & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                        i |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                    }
                    trafficIncident.title_ = this.title_;
                    if ((i2 & 4) == 4) {
                        i |= 4;
                    }
                    trafficIncident.description_ = this.description_;
                    if ((i2 & TrafficIncident.UID_FIELD_NUMBER) == TrafficIncident.UID_FIELD_NUMBER) {
                        i |= TrafficIncident.UID_FIELD_NUMBER;
                    }
                    trafficIncident.location_ = this.location_;
                    if ((i2 & TrafficIncident.LASTUPDATED_FIELD_NUMBER) == TrafficIncident.LASTUPDATED_FIELD_NUMBER) {
                        i |= TrafficIncident.LASTUPDATED_FIELD_NUMBER;
                    }
                    trafficIncident.vertexOffset_ = this.vertexOffset_;
                    if ((i2 & 32) == 32) {
                        i |= 32;
                    }
                    trafficIncident.incidentVertex_ = this.incidentVertex_;
                    if ((i2 & 64) == 64) {
                        i |= 64;
                    }
                    trafficIncident.startTime_ = this.startTime_;
                    if ((i2 & Flags.FLAG8) == Flags.FLAG8) {
                        i |= Flags.FLAG8;
                    }
                    trafficIncident.endTime_ = this.endTime_;
                    if ((i2 & KEYRecord.OWNER_ZONE) == KEYRecord.OWNER_ZONE) {
                        i |= KEYRecord.OWNER_ZONE;
                    }
                    trafficIncident.lastUpdated_ = this.lastUpdated_;
                    if ((i2 & KEYRecord.OWNER_HOST) == KEYRecord.OWNER_HOST) {
                        i |= KEYRecord.OWNER_HOST;
                    }
                    trafficIncident.type_ = this.type_;
                    trafficIncident.bitField0_ = i;
                    onBuilt();
                    return trafficIncident;
                }

                public Builder mergeFrom(Message message) {
                    if (message instanceof TrafficIncident) {
                        return mergeFrom((TrafficIncident) message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder mergeFrom(TrafficIncident trafficIncident) {
                    if (trafficIncident != TrafficIncident.getDefaultInstance()) {
                        if (trafficIncident.hasUID()) {
                            setUID(trafficIncident.getUID());
                        }
                        if (trafficIncident.hasTitle()) {
                            setTitle(trafficIncident.getTitle());
                        }
                        if (trafficIncident.hasDescription()) {
                            setDescription(trafficIncident.getDescription());
                        }
                        if (trafficIncident.hasLocation()) {
                            setLocation(trafficIncident.getLocation());
                        }
                        if (trafficIncident.hasVertexOffset()) {
                            setVertexOffset(trafficIncident.getVertexOffset());
                        }
                        if (trafficIncident.hasIncidentVertex()) {
                            setIncidentVertex(trafficIncident.getIncidentVertex());
                        }
                        if (trafficIncident.hasStartTime()) {
                            setStartTime(trafficIncident.getStartTime());
                        }
                        if (trafficIncident.hasEndTime()) {
                            setEndTime(trafficIncident.getEndTime());
                        }
                        if (trafficIncident.hasLastUpdated()) {
                            setLastUpdated(trafficIncident.getLastUpdated());
                        }
                        if (trafficIncident.hasType()) {
                            setType(trafficIncident.getType());
                        }
                        mergeUnknownFields(trafficIncident.getUnknownFields());
                    }
                    return this;
                }

                public final boolean isInitialized() {
                    return true;
                }

                public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                    while (true) {
                        int readTag = codedInputStream.readTag();
                        switch (readTag) {
                            case KEYRecord.OWNER_USER /*0*/:
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            case WbxmlParser.WAP_EXTENSION /*64*/:
                                this.bitField0_ |= TrafficTile.VERTICES_FIELD_NUMBER;
                                this.uID_ = codedInputStream.readInt64();
                                continue;
                            case Service.NETRJS_4 /*74*/:
                                this.bitField0_ |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                                this.title_ = codedInputStream.readBytes();
                                continue;
                            case Opcodes.DASTORE /*82*/:
                                this.bitField0_ |= 4;
                                this.description_ = codedInputStream.readBytes();
                                continue;
                            case Opcodes.DUP_X1 /*90*/:
                                this.bitField0_ |= TrafficIncident.UID_FIELD_NUMBER;
                                this.location_ = codedInputStream.readBytes();
                                continue;
                            case SmileConstants.TOKEN_PREFIX_SMALL_ASCII /*96*/:
                                this.bitField0_ |= TrafficIncident.LASTUPDATED_FIELD_NUMBER;
                                this.vertexOffset_ = codedInputStream.readInt32();
                                continue;
                            case Opcodes.FMUL /*106*/:
                                this.bitField0_ |= 32;
                                this.incidentVertex_ = codedInputStream.readBytes();
                                continue;
                            case Service.AUTH /*113*/:
                                this.bitField0_ |= 64;
                                this.startTime_ = codedInputStream.readDouble();
                                continue;
                            case Service.ERPC /*121*/:
                                this.bitField0_ |= Flags.FLAG8;
                                this.endTime_ = codedInputStream.readDouble();
                                continue;
                            case Service.PWDGEN /*129*/:
                                this.bitField0_ |= KEYRecord.OWNER_ZONE;
                                this.lastUpdated_ = codedInputStream.readDouble();
                                continue;
                            case Service.PROFILE /*136*/:
                                readTag = codedInputStream.readEnum();
                                Type valueOf = Type.valueOf(readTag);
                                if (valueOf != null) {
                                    this.bitField0_ |= KEYRecord.OWNER_HOST;
                                    this.type_ = valueOf;
                                    break;
                                }
                                newBuilder.mergeVarintField(TrafficIncident.TYPE_FIELD_NUMBER, readTag);
                                continue;
                            default:
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                    setUnknownFields(newBuilder.build());
                                    onChanged();
                                    break;
                                }
                                continue;
                        }
                        return this;
                    }
                }

                public boolean hasUID() {
                    return (this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER;
                }

                public long getUID() {
                    return this.uID_;
                }

                public Builder setUID(long j) {
                    this.bitField0_ |= TrafficTile.VERTICES_FIELD_NUMBER;
                    this.uID_ = j;
                    onChanged();
                    return this;
                }

                public Builder clearUID() {
                    this.bitField0_ &= -2;
                    this.uID_ = 0;
                    onChanged();
                    return this;
                }

                public boolean hasTitle() {
                    return (this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                }

                public String getTitle() {
                    Object obj = this.title_;
                    if (obj instanceof String) {
                        return (String) obj;
                    }
                    String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.title_ = toStringUtf8;
                    return toStringUtf8;
                }

                public Builder setTitle(String str) {
                    if (str == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                    this.title_ = str;
                    onChanged();
                    return this;
                }

                public Builder clearTitle() {
                    this.bitField0_ &= -3;
                    this.title_ = TrafficIncident.getDefaultInstance().getTitle();
                    onChanged();
                    return this;
                }

                void setTitle(ByteString byteString) {
                    this.bitField0_ |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                    this.title_ = byteString;
                    onChanged();
                }

                public boolean hasDescription() {
                    return (this.bitField0_ & 4) == 4;
                }

                public String getDescription() {
                    Object obj = this.description_;
                    if (obj instanceof String) {
                        return (String) obj;
                    }
                    String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.description_ = toStringUtf8;
                    return toStringUtf8;
                }

                public Builder setDescription(String str) {
                    if (str == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 4;
                    this.description_ = str;
                    onChanged();
                    return this;
                }

                public Builder clearDescription() {
                    this.bitField0_ &= -5;
                    this.description_ = TrafficIncident.getDefaultInstance().getDescription();
                    onChanged();
                    return this;
                }

                void setDescription(ByteString byteString) {
                    this.bitField0_ |= 4;
                    this.description_ = byteString;
                    onChanged();
                }

                public boolean hasLocation() {
                    return (this.bitField0_ & TrafficIncident.UID_FIELD_NUMBER) == TrafficIncident.UID_FIELD_NUMBER;
                }

                public String getLocation() {
                    Object obj = this.location_;
                    if (obj instanceof String) {
                        return (String) obj;
                    }
                    String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.location_ = toStringUtf8;
                    return toStringUtf8;
                }

                public Builder setLocation(String str) {
                    if (str == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= TrafficIncident.UID_FIELD_NUMBER;
                    this.location_ = str;
                    onChanged();
                    return this;
                }

                public Builder clearLocation() {
                    this.bitField0_ &= -9;
                    this.location_ = TrafficIncident.getDefaultInstance().getLocation();
                    onChanged();
                    return this;
                }

                void setLocation(ByteString byteString) {
                    this.bitField0_ |= TrafficIncident.UID_FIELD_NUMBER;
                    this.location_ = byteString;
                    onChanged();
                }

                public boolean hasVertexOffset() {
                    return (this.bitField0_ & TrafficIncident.LASTUPDATED_FIELD_NUMBER) == TrafficIncident.LASTUPDATED_FIELD_NUMBER;
                }

                public int getVertexOffset() {
                    return this.vertexOffset_;
                }

                public Builder setVertexOffset(int i) {
                    this.bitField0_ |= TrafficIncident.LASTUPDATED_FIELD_NUMBER;
                    this.vertexOffset_ = i;
                    onChanged();
                    return this;
                }

                public Builder clearVertexOffset() {
                    this.bitField0_ &= -17;
                    this.vertexOffset_ = 0;
                    onChanged();
                    return this;
                }

                public boolean hasIncidentVertex() {
                    return (this.bitField0_ & 32) == 32;
                }

                public ByteString getIncidentVertex() {
                    return this.incidentVertex_;
                }

                public Builder setIncidentVertex(ByteString byteString) {
                    if (byteString == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 32;
                    this.incidentVertex_ = byteString;
                    onChanged();
                    return this;
                }

                public Builder clearIncidentVertex() {
                    this.bitField0_ &= -33;
                    this.incidentVertex_ = TrafficIncident.getDefaultInstance().getIncidentVertex();
                    onChanged();
                    return this;
                }

                public boolean hasStartTime() {
                    return (this.bitField0_ & 64) == 64;
                }

                public double getStartTime() {
                    return this.startTime_;
                }

                public Builder setStartTime(double d) {
                    this.bitField0_ |= 64;
                    this.startTime_ = d;
                    onChanged();
                    return this;
                }

                public Builder clearStartTime() {
                    this.bitField0_ &= -65;
                    this.startTime_ = 0.0d;
                    onChanged();
                    return this;
                }

                public boolean hasEndTime() {
                    return (this.bitField0_ & Flags.FLAG8) == Flags.FLAG8;
                }

                public double getEndTime() {
                    return this.endTime_;
                }

                public Builder setEndTime(double d) {
                    this.bitField0_ |= Flags.FLAG8;
                    this.endTime_ = d;
                    onChanged();
                    return this;
                }

                public Builder clearEndTime() {
                    this.bitField0_ &= -129;
                    this.endTime_ = 0.0d;
                    onChanged();
                    return this;
                }

                public boolean hasLastUpdated() {
                    return (this.bitField0_ & KEYRecord.OWNER_ZONE) == KEYRecord.OWNER_ZONE;
                }

                public double getLastUpdated() {
                    return this.lastUpdated_;
                }

                public Builder setLastUpdated(double d) {
                    this.bitField0_ |= KEYRecord.OWNER_ZONE;
                    this.lastUpdated_ = d;
                    onChanged();
                    return this;
                }

                public Builder clearLastUpdated() {
                    this.bitField0_ &= -257;
                    this.lastUpdated_ = 0.0d;
                    onChanged();
                    return this;
                }

                public boolean hasType() {
                    return (this.bitField0_ & KEYRecord.OWNER_HOST) == KEYRecord.OWNER_HOST;
                }

                public Type getType() {
                    return this.type_;
                }

                public Builder setType(Type type) {
                    if (type == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= KEYRecord.OWNER_HOST;
                    this.type_ = type;
                    onChanged();
                    return this;
                }

                public Builder clearType() {
                    this.bitField0_ &= -513;
                    this.type_ = Type.CONSTRUCTION_LONG_TERM;
                    onChanged();
                    return this;
                }
            }

            private TrafficIncident(Builder builder) {
                super(builder);
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            private TrafficIncident(boolean z) {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            public static TrafficIncident getDefaultInstance() {
                return defaultInstance;
            }

            public TrafficIncident getDefaultInstanceForType() {
                return defaultInstance;
            }

            public static final Descriptor getDescriptor() {
                return TrafficProtos.internal_static_traffic_TrafficTile_TrafficIncident_descriptor;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return TrafficProtos.f509x10786b62;
            }

            public boolean hasUID() {
                return (this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER;
            }

            public long getUID() {
                return this.uID_;
            }

            public boolean hasTitle() {
                return (this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
            }

            public String getTitle() {
                Object obj = this.title_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                ByteString byteString = (ByteString) obj;
                String toStringUtf8 = byteString.toStringUtf8();
                if (Internal.isValidUtf8(byteString)) {
                    this.title_ = toStringUtf8;
                }
                return toStringUtf8;
            }

            private ByteString getTitleBytes() {
                Object obj = this.title_;
                if (!(obj instanceof String)) {
                    return (ByteString) obj;
                }
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.title_ = copyFromUtf8;
                return copyFromUtf8;
            }

            public boolean hasDescription() {
                return (this.bitField0_ & 4) == 4;
            }

            public String getDescription() {
                Object obj = this.description_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                ByteString byteString = (ByteString) obj;
                String toStringUtf8 = byteString.toStringUtf8();
                if (Internal.isValidUtf8(byteString)) {
                    this.description_ = toStringUtf8;
                }
                return toStringUtf8;
            }

            private ByteString getDescriptionBytes() {
                Object obj = this.description_;
                if (!(obj instanceof String)) {
                    return (ByteString) obj;
                }
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.description_ = copyFromUtf8;
                return copyFromUtf8;
            }

            public boolean hasLocation() {
                return (this.bitField0_ & UID_FIELD_NUMBER) == UID_FIELD_NUMBER;
            }

            public String getLocation() {
                Object obj = this.location_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                ByteString byteString = (ByteString) obj;
                String toStringUtf8 = byteString.toStringUtf8();
                if (Internal.isValidUtf8(byteString)) {
                    this.location_ = toStringUtf8;
                }
                return toStringUtf8;
            }

            private ByteString getLocationBytes() {
                Object obj = this.location_;
                if (!(obj instanceof String)) {
                    return (ByteString) obj;
                }
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.location_ = copyFromUtf8;
                return copyFromUtf8;
            }

            public boolean hasVertexOffset() {
                return (this.bitField0_ & LASTUPDATED_FIELD_NUMBER) == LASTUPDATED_FIELD_NUMBER;
            }

            public int getVertexOffset() {
                return this.vertexOffset_;
            }

            public boolean hasIncidentVertex() {
                return (this.bitField0_ & 32) == 32;
            }

            public ByteString getIncidentVertex() {
                return this.incidentVertex_;
            }

            public boolean hasStartTime() {
                return (this.bitField0_ & 64) == 64;
            }

            public double getStartTime() {
                return this.startTime_;
            }

            public boolean hasEndTime() {
                return (this.bitField0_ & Flags.FLAG8) == Flags.FLAG8;
            }

            public double getEndTime() {
                return this.endTime_;
            }

            public boolean hasLastUpdated() {
                return (this.bitField0_ & KEYRecord.OWNER_ZONE) == KEYRecord.OWNER_ZONE;
            }

            public double getLastUpdated() {
                return this.lastUpdated_;
            }

            public boolean hasType() {
                return (this.bitField0_ & KEYRecord.OWNER_HOST) == KEYRecord.OWNER_HOST;
            }

            public Type getType() {
                return this.type_;
            }

            private void initFields() {
                this.uID_ = 0;
                this.title_ = XmlPullParser.NO_NAMESPACE;
                this.description_ = XmlPullParser.NO_NAMESPACE;
                this.location_ = XmlPullParser.NO_NAMESPACE;
                this.vertexOffset_ = 0;
                this.incidentVertex_ = ByteString.EMPTY;
                this.startTime_ = 0.0d;
                this.endTime_ = 0.0d;
                this.lastUpdated_ = 0.0d;
                this.type_ = Type.CONSTRUCTION_LONG_TERM;
            }

            public final boolean isInitialized() {
                byte b = this.memoizedIsInitialized;
                if (b == -1) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                } else if (b == (byte) 1) {
                    return true;
                } else {
                    return false;
                }
            }

            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                getSerializedSize();
                if ((this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER) {
                    codedOutputStream.writeInt64(UID_FIELD_NUMBER, this.uID_);
                }
                if ((this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                    codedOutputStream.writeBytes(TITLE_FIELD_NUMBER, getTitleBytes());
                }
                if ((this.bitField0_ & 4) == 4) {
                    codedOutputStream.writeBytes(DESCRIPTION_FIELD_NUMBER, getDescriptionBytes());
                }
                if ((this.bitField0_ & UID_FIELD_NUMBER) == UID_FIELD_NUMBER) {
                    codedOutputStream.writeBytes(LOCATION_FIELD_NUMBER, getLocationBytes());
                }
                if ((this.bitField0_ & LASTUPDATED_FIELD_NUMBER) == LASTUPDATED_FIELD_NUMBER) {
                    codedOutputStream.writeInt32(VERTEXOFFSET_FIELD_NUMBER, this.vertexOffset_);
                }
                if ((this.bitField0_ & 32) == 32) {
                    codedOutputStream.writeBytes(INCIDENTVERTEX_FIELD_NUMBER, this.incidentVertex_);
                }
                if ((this.bitField0_ & 64) == 64) {
                    codedOutputStream.writeDouble(STARTTIME_FIELD_NUMBER, this.startTime_);
                }
                if ((this.bitField0_ & Flags.FLAG8) == Flags.FLAG8) {
                    codedOutputStream.writeDouble(ENDTIME_FIELD_NUMBER, this.endTime_);
                }
                if ((this.bitField0_ & KEYRecord.OWNER_ZONE) == KEYRecord.OWNER_ZONE) {
                    codedOutputStream.writeDouble(LASTUPDATED_FIELD_NUMBER, this.lastUpdated_);
                }
                if ((this.bitField0_ & KEYRecord.OWNER_HOST) == KEYRecord.OWNER_HOST) {
                    codedOutputStream.writeEnum(TYPE_FIELD_NUMBER, this.type_.getNumber());
                }
                getUnknownFields().writeTo(codedOutputStream);
            }

            public int getSerializedSize() {
                int i = this.memoizedSerializedSize;
                if (i != -1) {
                    return i;
                }
                i = 0;
                if ((this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER) {
                    i = 0 + CodedOutputStream.computeInt64Size(UID_FIELD_NUMBER, this.uID_);
                }
                if ((this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                    i += CodedOutputStream.computeBytesSize(TITLE_FIELD_NUMBER, getTitleBytes());
                }
                if ((this.bitField0_ & 4) == 4) {
                    i += CodedOutputStream.computeBytesSize(DESCRIPTION_FIELD_NUMBER, getDescriptionBytes());
                }
                if ((this.bitField0_ & UID_FIELD_NUMBER) == UID_FIELD_NUMBER) {
                    i += CodedOutputStream.computeBytesSize(LOCATION_FIELD_NUMBER, getLocationBytes());
                }
                if ((this.bitField0_ & LASTUPDATED_FIELD_NUMBER) == LASTUPDATED_FIELD_NUMBER) {
                    i += CodedOutputStream.computeInt32Size(VERTEXOFFSET_FIELD_NUMBER, this.vertexOffset_);
                }
                if ((this.bitField0_ & 32) == 32) {
                    i += CodedOutputStream.computeBytesSize(INCIDENTVERTEX_FIELD_NUMBER, this.incidentVertex_);
                }
                if ((this.bitField0_ & 64) == 64) {
                    i += CodedOutputStream.computeDoubleSize(STARTTIME_FIELD_NUMBER, this.startTime_);
                }
                if ((this.bitField0_ & Flags.FLAG8) == Flags.FLAG8) {
                    i += CodedOutputStream.computeDoubleSize(ENDTIME_FIELD_NUMBER, this.endTime_);
                }
                if ((this.bitField0_ & KEYRecord.OWNER_ZONE) == KEYRecord.OWNER_ZONE) {
                    i += CodedOutputStream.computeDoubleSize(LASTUPDATED_FIELD_NUMBER, this.lastUpdated_);
                }
                if ((this.bitField0_ & KEYRecord.OWNER_HOST) == KEYRecord.OWNER_HOST) {
                    i += CodedOutputStream.computeEnumSize(TYPE_FIELD_NUMBER, this.type_.getNumber());
                }
                i += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = i;
                return i;
            }

            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public static TrafficIncident parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
            }

            public static TrafficIncident parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
            }

            public static TrafficIncident parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
            }

            public static TrafficIncident parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
            }

            public static TrafficIncident parseFrom(InputStream inputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
            }

            public static TrafficIncident parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
            }

            public static TrafficIncident parseDelimitedFrom(InputStream inputStream) throws IOException {
                Builder newBuilder = newBuilder();
                if (newBuilder.mergeDelimitedFrom(inputStream)) {
                    return newBuilder.buildParsed();
                }
                return null;
            }

            public static TrafficIncident parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Builder newBuilder = newBuilder();
                if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                    return newBuilder.buildParsed();
                }
                return null;
            }

            public static TrafficIncident parseFrom(CodedInputStream codedInputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
            }

            public static TrafficIncident parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public Builder newBuilderForType() {
                return newBuilder();
            }

            public static Builder newBuilder(TrafficIncident trafficIncident) {
                return newBuilder().mergeFrom(trafficIncident);
            }

            public Builder toBuilder() {
                return newBuilder(this);
            }

            protected Builder newBuilderForType(BuilderParent builderParent) {
                return new Builder(null);
            }

            static {
                defaultInstance = new TrafficIncident(true);
                defaultInstance.initFields();
            }
        }

        public static final class TrafficSegment extends GeneratedMessage implements TrafficSegmentOrBuilder {
            public static final int SPEED_FIELD_NUMBER = 5;
            public static final int VERTEXCOUNT_FIELD_NUMBER = 4;
            public static final int VERTEXOFFSET_FIELD_NUMBER = 3;
            public static final int WIDTH_FIELD_NUMBER = 6;
            private static final TrafficSegment defaultInstance;
            private static final long serialVersionUID = 0;
            private int bitField0_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private TrafficSpeed speed_;
            private int vertexCount_;
            private int vertexOffset_;
            private int width_;

            public enum TrafficSpeed implements ProtocolMessageEnum {
                SLOW(0, SLOW_VALUE),
                MEDIUM(SLOW_VALUE, MEDIUM_VALUE),
                FAST(MEDIUM_VALUE, FAST_VALUE);
                
                public static final int FAST_VALUE = 3;
                public static final int MEDIUM_VALUE = 2;
                public static final int SLOW_VALUE = 1;
                private static final TrafficSpeed[] VALUES;
                private static EnumLiteMap<TrafficSpeed> internalValueMap;
                private final int index;
                private final int value;

                static {
                    internalValueMap = new ba();
                    TrafficSpeed[] trafficSpeedArr = new TrafficSpeed[FAST_VALUE];
                    trafficSpeedArr[0] = SLOW;
                    trafficSpeedArr[SLOW_VALUE] = MEDIUM;
                    trafficSpeedArr[MEDIUM_VALUE] = FAST;
                    VALUES = trafficSpeedArr;
                }

                public final int getNumber() {
                    return this.value;
                }

                public static TrafficSpeed valueOf(int i) {
                    switch (i) {
                        case SLOW_VALUE:
                            return SLOW;
                        case MEDIUM_VALUE:
                            return MEDIUM;
                        case FAST_VALUE:
                            return FAST;
                        default:
                            return null;
                    }
                }

                public static EnumLiteMap<TrafficSpeed> internalGetValueMap() {
                    return internalValueMap;
                }

                public final EnumValueDescriptor getValueDescriptor() {
                    return (EnumValueDescriptor) getDescriptor().getValues().get(this.index);
                }

                public final EnumDescriptor getDescriptorForType() {
                    return getDescriptor();
                }

                public static final EnumDescriptor getDescriptor() {
                    return (EnumDescriptor) TrafficSegment.getDescriptor().getEnumTypes().get(0);
                }

                public static TrafficSpeed valueOf(EnumValueDescriptor enumValueDescriptor) {
                    if (enumValueDescriptor.getType() == getDescriptor()) {
                        return VALUES[enumValueDescriptor.getIndex()];
                    }
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }

                private TrafficSpeed(int i, int i2) {
                    this.index = i;
                    this.value = i2;
                }
            }

            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements TrafficSegmentOrBuilder {
                private int bitField0_;
                private TrafficSpeed speed_;
                private int vertexCount_;
                private int vertexOffset_;
                private int width_;

                public static final Descriptor getDescriptor() {
                    return TrafficProtos.internal_static_traffic_TrafficTile_TrafficSegment_descriptor;
                }

                protected FieldAccessorTable internalGetFieldAccessorTable() {
                    return TrafficProtos.f510xbb770691;
                }

                private Builder() {
                    this.speed_ = TrafficSpeed.FAST;
                    maybeForceBuilderInitialization();
                }

                private Builder(BuilderParent builderParent) {
                    super(builderParent);
                    this.speed_ = TrafficSpeed.FAST;
                    maybeForceBuilderInitialization();
                }

                private void maybeForceBuilderInitialization() {
                    if (!TrafficSegment.alwaysUseFieldBuilders) {
                    }
                }

                private static Builder create() {
                    return new Builder();
                }

                public Builder clear() {
                    super.clear();
                    this.vertexOffset_ = 0;
                    this.bitField0_ &= -2;
                    this.vertexCount_ = 0;
                    this.bitField0_ &= -3;
                    this.speed_ = TrafficSpeed.FAST;
                    this.bitField0_ &= -5;
                    this.width_ = 0;
                    this.bitField0_ &= -9;
                    return this;
                }

                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                public Descriptor getDescriptorForType() {
                    return TrafficSegment.getDescriptor();
                }

                public TrafficSegment getDefaultInstanceForType() {
                    return TrafficSegment.getDefaultInstance();
                }

                public TrafficSegment build() {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
                }

                private TrafficSegment buildParsed() throws InvalidProtocolBufferException {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
                }

                public TrafficSegment buildPartial() {
                    int i = TrafficTile.VERTICES_FIELD_NUMBER;
                    TrafficSegment trafficSegment = new TrafficSegment();
                    int i2 = this.bitField0_;
                    if ((i2 & TrafficTile.VERTICES_FIELD_NUMBER) != TrafficTile.VERTICES_FIELD_NUMBER) {
                        i = 0;
                    }
                    trafficSegment.vertexOffset_ = this.vertexOffset_;
                    if ((i2 & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                        i |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                    }
                    trafficSegment.vertexCount_ = this.vertexCount_;
                    if ((i2 & TrafficSegment.VERTEXCOUNT_FIELD_NUMBER) == TrafficSegment.VERTEXCOUNT_FIELD_NUMBER) {
                        i |= TrafficSegment.VERTEXCOUNT_FIELD_NUMBER;
                    }
                    trafficSegment.speed_ = this.speed_;
                    if ((i2 & 8) == 8) {
                        i |= 8;
                    }
                    trafficSegment.width_ = this.width_;
                    trafficSegment.bitField0_ = i;
                    onBuilt();
                    return trafficSegment;
                }

                public Builder mergeFrom(Message message) {
                    if (message instanceof TrafficSegment) {
                        return mergeFrom((TrafficSegment) message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder mergeFrom(TrafficSegment trafficSegment) {
                    if (trafficSegment != TrafficSegment.getDefaultInstance()) {
                        if (trafficSegment.hasVertexOffset()) {
                            setVertexOffset(trafficSegment.getVertexOffset());
                        }
                        if (trafficSegment.hasVertexCount()) {
                            setVertexCount(trafficSegment.getVertexCount());
                        }
                        if (trafficSegment.hasSpeed()) {
                            setSpeed(trafficSegment.getSpeed());
                        }
                        if (trafficSegment.hasWidth()) {
                            setWidth(trafficSegment.getWidth());
                        }
                        mergeUnknownFields(trafficSegment.getUnknownFields());
                    }
                    return this;
                }

                public final boolean isInitialized() {
                    if (hasVertexOffset() && hasVertexCount()) {
                        return true;
                    }
                    return false;
                }

                public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                    while (true) {
                        int readTag = codedInputStream.readTag();
                        switch (readTag) {
                            case KEYRecord.OWNER_USER /*0*/:
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            case Protocol.TRUNK_2 /*24*/:
                                this.bitField0_ |= TrafficTile.VERTICES_FIELD_NUMBER;
                                this.vertexOffset_ = codedInputStream.readInt32();
                                continue;
                            case Protocol.MERIT_INP /*32*/:
                                this.bitField0_ |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                                this.vertexCount_ = codedInputStream.readInt32();
                                continue;
                            case SmileConstants.TOKEN_MISC_FP /*40*/:
                                readTag = codedInputStream.readEnum();
                                TrafficSpeed valueOf = TrafficSpeed.valueOf(readTag);
                                if (valueOf != null) {
                                    this.bitField0_ |= TrafficSegment.VERTEXCOUNT_FIELD_NUMBER;
                                    this.speed_ = valueOf;
                                    break;
                                }
                                newBuilder.mergeVarintField(TrafficSegment.SPEED_FIELD_NUMBER, readTag);
                                continue;
                            case Type.DNSKEY /*48*/:
                                this.bitField0_ |= 8;
                                this.width_ = codedInputStream.readInt32();
                                continue;
                            default:
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                    setUnknownFields(newBuilder.build());
                                    onChanged();
                                    break;
                                }
                                continue;
                        }
                        return this;
                    }
                }

                public boolean hasVertexOffset() {
                    return (this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER;
                }

                public int getVertexOffset() {
                    return this.vertexOffset_;
                }

                public Builder setVertexOffset(int i) {
                    this.bitField0_ |= TrafficTile.VERTICES_FIELD_NUMBER;
                    this.vertexOffset_ = i;
                    onChanged();
                    return this;
                }

                public Builder clearVertexOffset() {
                    this.bitField0_ &= -2;
                    this.vertexOffset_ = 0;
                    onChanged();
                    return this;
                }

                public boolean hasVertexCount() {
                    return (this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                }

                public int getVertexCount() {
                    return this.vertexCount_;
                }

                public Builder setVertexCount(int i) {
                    this.bitField0_ |= TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
                    this.vertexCount_ = i;
                    onChanged();
                    return this;
                }

                public Builder clearVertexCount() {
                    this.bitField0_ &= -3;
                    this.vertexCount_ = 0;
                    onChanged();
                    return this;
                }

                public boolean hasSpeed() {
                    return (this.bitField0_ & TrafficSegment.VERTEXCOUNT_FIELD_NUMBER) == TrafficSegment.VERTEXCOUNT_FIELD_NUMBER;
                }

                public TrafficSpeed getSpeed() {
                    return this.speed_;
                }

                public Builder setSpeed(TrafficSpeed trafficSpeed) {
                    if (trafficSpeed == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= TrafficSegment.VERTEXCOUNT_FIELD_NUMBER;
                    this.speed_ = trafficSpeed;
                    onChanged();
                    return this;
                }

                public Builder clearSpeed() {
                    this.bitField0_ &= -5;
                    this.speed_ = TrafficSpeed.FAST;
                    onChanged();
                    return this;
                }

                public boolean hasWidth() {
                    return (this.bitField0_ & 8) == 8;
                }

                public int getWidth() {
                    return this.width_;
                }

                public Builder setWidth(int i) {
                    this.bitField0_ |= 8;
                    this.width_ = i;
                    onChanged();
                    return this;
                }

                public Builder clearWidth() {
                    this.bitField0_ &= -9;
                    this.width_ = 0;
                    onChanged();
                    return this;
                }
            }

            private TrafficSegment(Builder builder) {
                super(builder);
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            private TrafficSegment(boolean z) {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            public static TrafficSegment getDefaultInstance() {
                return defaultInstance;
            }

            public TrafficSegment getDefaultInstanceForType() {
                return defaultInstance;
            }

            public static final Descriptor getDescriptor() {
                return TrafficProtos.internal_static_traffic_TrafficTile_TrafficSegment_descriptor;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return TrafficProtos.f510xbb770691;
            }

            public boolean hasVertexOffset() {
                return (this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER;
            }

            public int getVertexOffset() {
                return this.vertexOffset_;
            }

            public boolean hasVertexCount() {
                return (this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER;
            }

            public int getVertexCount() {
                return this.vertexCount_;
            }

            public boolean hasSpeed() {
                return (this.bitField0_ & VERTEXCOUNT_FIELD_NUMBER) == VERTEXCOUNT_FIELD_NUMBER;
            }

            public TrafficSpeed getSpeed() {
                return this.speed_;
            }

            public boolean hasWidth() {
                return (this.bitField0_ & 8) == 8;
            }

            public int getWidth() {
                return this.width_;
            }

            private void initFields() {
                this.vertexOffset_ = 0;
                this.vertexCount_ = 0;
                this.speed_ = TrafficSpeed.FAST;
                this.width_ = 0;
            }

            public final boolean isInitialized() {
                byte b = this.memoizedIsInitialized;
                if (b != -1) {
                    if (b == (byte) 1) {
                        return true;
                    }
                    return false;
                } else if (!hasVertexOffset()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                } else if (hasVertexCount()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                } else {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }

            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                getSerializedSize();
                if ((this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER) {
                    codedOutputStream.writeInt32(VERTEXOFFSET_FIELD_NUMBER, this.vertexOffset_);
                }
                if ((this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                    codedOutputStream.writeInt32(VERTEXCOUNT_FIELD_NUMBER, this.vertexCount_);
                }
                if ((this.bitField0_ & VERTEXCOUNT_FIELD_NUMBER) == VERTEXCOUNT_FIELD_NUMBER) {
                    codedOutputStream.writeEnum(SPEED_FIELD_NUMBER, this.speed_.getNumber());
                }
                if ((this.bitField0_ & 8) == 8) {
                    codedOutputStream.writeInt32(WIDTH_FIELD_NUMBER, this.width_);
                }
                getUnknownFields().writeTo(codedOutputStream);
            }

            public int getSerializedSize() {
                int i = this.memoizedSerializedSize;
                if (i != -1) {
                    return i;
                }
                i = 0;
                if ((this.bitField0_ & TrafficTile.VERTICES_FIELD_NUMBER) == TrafficTile.VERTICES_FIELD_NUMBER) {
                    i = 0 + CodedOutputStream.computeInt32Size(VERTEXOFFSET_FIELD_NUMBER, this.vertexOffset_);
                }
                if ((this.bitField0_ & TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) == TrafficTile.TRAFFICSEGMENT_FIELD_NUMBER) {
                    i += CodedOutputStream.computeInt32Size(VERTEXCOUNT_FIELD_NUMBER, this.vertexCount_);
                }
                if ((this.bitField0_ & VERTEXCOUNT_FIELD_NUMBER) == VERTEXCOUNT_FIELD_NUMBER) {
                    i += CodedOutputStream.computeEnumSize(SPEED_FIELD_NUMBER, this.speed_.getNumber());
                }
                if ((this.bitField0_ & 8) == 8) {
                    i += CodedOutputStream.computeInt32Size(WIDTH_FIELD_NUMBER, this.width_);
                }
                i += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = i;
                return i;
            }

            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public static TrafficSegment parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
            }

            public static TrafficSegment parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
            }

            public static TrafficSegment parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
            }

            public static TrafficSegment parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
            }

            public static TrafficSegment parseFrom(InputStream inputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
            }

            public static TrafficSegment parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
            }

            public static TrafficSegment parseDelimitedFrom(InputStream inputStream) throws IOException {
                Builder newBuilder = newBuilder();
                if (newBuilder.mergeDelimitedFrom(inputStream)) {
                    return newBuilder.buildParsed();
                }
                return null;
            }

            public static TrafficSegment parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Builder newBuilder = newBuilder();
                if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                    return newBuilder.buildParsed();
                }
                return null;
            }

            public static TrafficSegment parseFrom(CodedInputStream codedInputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
            }

            public static TrafficSegment parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public Builder newBuilderForType() {
                return newBuilder();
            }

            public static Builder newBuilder(TrafficSegment trafficSegment) {
                return newBuilder().mergeFrom(trafficSegment);
            }

            public Builder toBuilder() {
                return newBuilder(this);
            }

            protected Builder newBuilderForType(BuilderParent builderParent) {
                return new Builder(null);
            }

            static {
                defaultInstance = new TrafficSegment(true);
                defaultInstance.initFields();
            }
        }

        private TrafficTile(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private TrafficTile(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static TrafficTile getDefaultInstance() {
            return defaultInstance;
        }

        public TrafficTile getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return TrafficProtos.internal_static_traffic_TrafficTile_descriptor;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return TrafficProtos.internal_static_traffic_TrafficTile_fieldAccessorTable;
        }

        public boolean hasVertices() {
            return (this.bitField0_ & VERTICES_FIELD_NUMBER) == VERTICES_FIELD_NUMBER;
        }

        public ByteString getVertices() {
            return this.vertices_;
        }

        public List<TrafficSegment> getTrafficSegmentList() {
            return this.trafficSegment_;
        }

        public List<? extends TrafficSegmentOrBuilder> getTrafficSegmentOrBuilderList() {
            return this.trafficSegment_;
        }

        public int getTrafficSegmentCount() {
            return this.trafficSegment_.size();
        }

        public TrafficSegment getTrafficSegment(int i) {
            return (TrafficSegment) this.trafficSegment_.get(i);
        }

        public TrafficSegmentOrBuilder getTrafficSegmentOrBuilder(int i) {
            return (TrafficSegmentOrBuilder) this.trafficSegment_.get(i);
        }

        public List<TrafficIncident> getTrafficIncidentList() {
            return this.trafficIncident_;
        }

        public List<? extends TrafficIncidentOrBuilder> getTrafficIncidentOrBuilderList() {
            return this.trafficIncident_;
        }

        public int getTrafficIncidentCount() {
            return this.trafficIncident_.size();
        }

        public TrafficIncident getTrafficIncident(int i) {
            return (TrafficIncident) this.trafficIncident_.get(i);
        }

        public TrafficIncidentOrBuilder getTrafficIncidentOrBuilder(int i) {
            return (TrafficIncidentOrBuilder) this.trafficIncident_.get(i);
        }

        private void initFields() {
            this.vertices_ = ByteString.EMPTY;
            this.trafficSegment_ = Collections.emptyList();
            this.trafficIncident_ = Collections.emptyList();
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == -1) {
                int i = 0;
                while (i < getTrafficSegmentCount()) {
                    if (getTrafficSegment(i).isInitialized()) {
                        i += VERTICES_FIELD_NUMBER;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                this.memoizedIsInitialized = (byte) 1;
                return true;
            } else if (b == (byte) 1) {
                return true;
            } else {
                return false;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            int i = 0;
            getSerializedSize();
            if ((this.bitField0_ & VERTICES_FIELD_NUMBER) == VERTICES_FIELD_NUMBER) {
                codedOutputStream.writeBytes(VERTICES_FIELD_NUMBER, this.vertices_);
            }
            for (int i2 = 0; i2 < this.trafficSegment_.size(); i2 += VERTICES_FIELD_NUMBER) {
                codedOutputStream.writeGroup(TRAFFICSEGMENT_FIELD_NUMBER, (MessageLite) this.trafficSegment_.get(i2));
            }
            while (i < this.trafficIncident_.size()) {
                codedOutputStream.writeGroup(TRAFFICINCIDENT_FIELD_NUMBER, (MessageLite) this.trafficIncident_.get(i));
                i += VERTICES_FIELD_NUMBER;
            }
            getUnknownFields().writeTo(codedOutputStream);
        }

        public int getSerializedSize() {
            int i = 0;
            int i2 = this.memoizedSerializedSize;
            if (i2 != -1) {
                return i2;
            }
            if ((this.bitField0_ & VERTICES_FIELD_NUMBER) == VERTICES_FIELD_NUMBER) {
                i2 = CodedOutputStream.computeBytesSize(VERTICES_FIELD_NUMBER, this.vertices_) + 0;
            } else {
                i2 = 0;
            }
            int i3 = i2;
            for (int i4 = 0; i4 < this.trafficSegment_.size(); i4 += VERTICES_FIELD_NUMBER) {
                i3 += CodedOutputStream.computeGroupSize(TRAFFICSEGMENT_FIELD_NUMBER, (MessageLite) this.trafficSegment_.get(i4));
            }
            while (i < this.trafficIncident_.size()) {
                i3 += CodedOutputStream.computeGroupSize(TRAFFICINCIDENT_FIELD_NUMBER, (MessageLite) this.trafficIncident_.get(i));
                i += VERTICES_FIELD_NUMBER;
            }
            i2 = getUnknownFields().getSerializedSize() + i3;
            this.memoizedSerializedSize = i2;
            return i2;
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static TrafficTile parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static TrafficTile parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static TrafficTile parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static TrafficTile parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static TrafficTile parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static TrafficTile parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static TrafficTile parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static TrafficTile parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static TrafficTile parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static TrafficTile parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(TrafficTile trafficTile) {
            return newBuilder().mergeFrom(trafficTile);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(null);
        }

        static {
            defaultInstance = new TrafficTile(true);
            defaultInstance.initFields();
        }
    }

    private TrafficProtos() {
    }

    public static void registerAllExtensions(ExtensionRegistry extensionRegistry) {
    }

    public static FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        FileDescriptor.internalBuildGeneratedFileFrom(new String[]{"\n\rtraffic.proto\u0012\u0007traffic\"\u00ed\u0005\n\u000bTrafficTile\u0012\u0010\n\bvertices\u0018\u0001 \u0001(\f\u0012;\n\u000etrafficsegment\u0018\u0002 \u0003(\n2#.traffic.TrafficTile.TrafficSegment\u0012=\n\u000ftrafficincident\u0018\u0007 \u0003(\n2$.traffic.TrafficTile.TrafficIncident\u001a\u00c1\u0001\n\u000eTrafficSegment\u0012\u0014\n\fvertexOffset\u0018\u0003 \u0002(\u0005\u0012\u0013\n\u000bvertexCount\u0018\u0004 \u0002(\u0005\u0012E\n\u0005speed\u0018\u0005 \u0001(\u000e20.traffic.TrafficTile.TrafficSegment.TrafficSpeed:\u0004FAST\u0012\r\n\u0005width\u0018\u0006 \u0001(\u0005\".\n\fTrafficSpeed\u0012\b\n\u0004SLOW\u0010\u0001\u0012\n\n\u0006MEDIUM\u0010\u0002\u0012\b\n\u0004FAST\u0010\u0003\u001a\u008b\u0003\n\u000fTrafficIncident\u0012\u000b", "\n\u0003UID\u0018\b \u0001(\u0003\u0012\r\n\u0005title\u0018\t \u0001(\t\u0012\u0013\n\u000bdescription\u0018\n \u0001(\t\u0012\u0010\n\blocation\u0018\u000b \u0001(\t\u0012\u0014\n\fvertexOffset\u0018\f \u0001(\u0005\u0012\u0016\n\u000eincidentVertex\u0018\r \u0001(\f\u0012\u0011\n\tstartTime\u0018\u000e \u0001(\u0001\u0012\u000f\n\u0007endTime\u0018\u000f \u0001(\u0001\u0012\u0013\n\u000blastUpdated\u0018\u0010 \u0001(\u0001\u00127\n\u0004type\u0018\u0011 \u0001(\u000e2).traffic.TrafficTile.TrafficIncident.Type\"\u0094\u0001\n\u0004Type\u0012\u001a\n\u0016CONSTRUCTION_LONG_TERM\u0010\u0001\u0012\u001b\n\u0017CONSTRUCTION_SHORT_TERM\u0010\u0002\u0012\u0010\n\fROAD_CLOSURE\u0010\u0003\u0012\u0010\n\fLANE_CLOSURE\u0010\u0004\u0012\u000b\n\u0007VEHICLE\u0010\u0005\u0012\n\n\u0006DEBRIS\u0010\u0006\u0012\u000b\n\u0007WEATHER\u0010\u0007\u0012\t\n\u0005EVENT\u0010\bB&\n\u0015com.amap.mapapi.ma", "pB\rTrafficProtos"}, new FileDescriptor[0], new ay());
    }
}
