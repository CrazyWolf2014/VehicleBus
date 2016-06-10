package org.jivesoftware.smackx.pubsub;

import com.ifoer.mine.Contact;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DataForm;

public class ConfigureForm extends Form {
    public ConfigureForm(DataForm dataForm) {
        super(dataForm);
    }

    public ConfigureForm(Form form) {
        super(form.getDataFormToSend());
    }

    public ConfigureForm(FormType formType) {
        super(formType.toString());
    }

    public AccessModel getAccessModel() {
        String fieldValue = getFieldValue(ConfigureNodeFields.access_model);
        if (fieldValue == null) {
            return null;
        }
        return AccessModel.valueOf(fieldValue);
    }

    public void setAccessModel(AccessModel accessModel) {
        addField(ConfigureNodeFields.access_model, FormField.TYPE_LIST_SINGLE);
        setAnswer(ConfigureNodeFields.access_model.getFieldName(), getListSingle(accessModel.toString()));
    }

    public String getBodyXSLT() {
        return getFieldValue(ConfigureNodeFields.body_xslt);
    }

    public void setBodyXSLT(String str) {
        addField(ConfigureNodeFields.body_xslt, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.body_xslt.getFieldName(), str);
    }

    public Iterator<String> getChildren() {
        return getFieldValues(ConfigureNodeFields.children);
    }

    public void setChildren(List<String> list) {
        addField(ConfigureNodeFields.children, FormField.TYPE_TEXT_MULTI);
        setAnswer(ConfigureNodeFields.children.getFieldName(), (List) list);
    }

    public ChildrenAssociationPolicy getChildrenAssociationPolicy() {
        String fieldValue = getFieldValue(ConfigureNodeFields.children_association_policy);
        if (fieldValue == null) {
            return null;
        }
        return ChildrenAssociationPolicy.valueOf(fieldValue);
    }

    public void setChildrenAssociationPolicy(ChildrenAssociationPolicy childrenAssociationPolicy) {
        addField(ConfigureNodeFields.children_association_policy, FormField.TYPE_LIST_SINGLE);
        List arrayList = new ArrayList(1);
        arrayList.add(childrenAssociationPolicy.toString());
        setAnswer(ConfigureNodeFields.children_association_policy.getFieldName(), arrayList);
    }

    public Iterator<String> getChildrenAssociationWhitelist() {
        return getFieldValues(ConfigureNodeFields.children_association_whitelist);
    }

    public void setChildrenAssociationWhitelist(List<String> list) {
        addField(ConfigureNodeFields.children_association_whitelist, FormField.TYPE_JID_MULTI);
        setAnswer(ConfigureNodeFields.children_association_whitelist.getFieldName(), (List) list);
    }

    public int getChildrenMax() {
        return Integer.parseInt(getFieldValue(ConfigureNodeFields.children_max));
    }

    public void setChildrenMax(int i) {
        addField(ConfigureNodeFields.children_max, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.children_max.getFieldName(), i);
    }

    public String getCollection() {
        return getFieldValue(ConfigureNodeFields.collection);
    }

    public void setCollection(String str) {
        addField(ConfigureNodeFields.collection, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.collection.getFieldName(), str);
    }

    public String getDataformXSLT() {
        return getFieldValue(ConfigureNodeFields.dataform_xslt);
    }

    public void setDataformXSLT(String str) {
        addField(ConfigureNodeFields.dataform_xslt, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.dataform_xslt.getFieldName(), str);
    }

    public boolean isDeliverPayloads() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.deliver_payloads));
    }

    public void setDeliverPayloads(boolean z) {
        addField(ConfigureNodeFields.deliver_payloads, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.deliver_payloads.getFieldName(), z);
    }

    public ItemReply getItemReply() {
        String fieldValue = getFieldValue(ConfigureNodeFields.itemreply);
        if (fieldValue == null) {
            return null;
        }
        return ItemReply.valueOf(fieldValue);
    }

    public void setItemReply(ItemReply itemReply) {
        addField(ConfigureNodeFields.itemreply, FormField.TYPE_LIST_SINGLE);
        setAnswer(ConfigureNodeFields.itemreply.getFieldName(), getListSingle(itemReply.toString()));
    }

    public int getMaxItems() {
        return Integer.parseInt(getFieldValue(ConfigureNodeFields.max_items));
    }

    public void setMaxItems(int i) {
        addField(ConfigureNodeFields.max_items, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.max_items.getFieldName(), i);
    }

    public int getMaxPayloadSize() {
        return Integer.parseInt(getFieldValue(ConfigureNodeFields.max_payload_size));
    }

    public void setMaxPayloadSize(int i) {
        addField(ConfigureNodeFields.max_payload_size, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.max_payload_size.getFieldName(), i);
    }

    public NodeType getNodeType() {
        String fieldValue = getFieldValue(ConfigureNodeFields.node_type);
        if (fieldValue == null) {
            return null;
        }
        return NodeType.valueOf(fieldValue);
    }

    public void setNodeType(NodeType nodeType) {
        addField(ConfigureNodeFields.node_type, FormField.TYPE_LIST_SINGLE);
        setAnswer(ConfigureNodeFields.node_type.getFieldName(), getListSingle(nodeType.toString()));
    }

    public boolean isNotifyConfig() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.notify_config));
    }

    public void setNotifyConfig(boolean z) {
        addField(ConfigureNodeFields.notify_config, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.notify_config.getFieldName(), z);
    }

    public boolean isNotifyDelete() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.notify_delete));
    }

    public void setNotifyDelete(boolean z) {
        addField(ConfigureNodeFields.notify_delete, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.notify_delete.getFieldName(), z);
    }

    public boolean isNotifyRetract() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.notify_retract));
    }

    public void setNotifyRetract(boolean z) {
        addField(ConfigureNodeFields.notify_retract, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.notify_retract.getFieldName(), z);
    }

    public boolean isPersistItems() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.persist_items));
    }

    public void setPersistentItems(boolean z) {
        addField(ConfigureNodeFields.persist_items, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.persist_items.getFieldName(), z);
    }

    public boolean isPresenceBasedDelivery() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.presence_based_delivery));
    }

    public void setPresenceBasedDelivery(boolean z) {
        addField(ConfigureNodeFields.presence_based_delivery, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.presence_based_delivery.getFieldName(), z);
    }

    public PublishModel getPublishModel() {
        String fieldValue = getFieldValue(ConfigureNodeFields.publish_model);
        if (fieldValue == null) {
            return null;
        }
        return PublishModel.valueOf(fieldValue);
    }

    public void setPublishModel(PublishModel publishModel) {
        addField(ConfigureNodeFields.publish_model, FormField.TYPE_LIST_SINGLE);
        setAnswer(ConfigureNodeFields.publish_model.getFieldName(), getListSingle(publishModel.toString()));
    }

    public Iterator<String> getReplyRoom() {
        return getFieldValues(ConfigureNodeFields.replyroom);
    }

    public void setReplyRoom(List<String> list) {
        addField(ConfigureNodeFields.replyroom, FormField.TYPE_LIST_MULTI);
        setAnswer(ConfigureNodeFields.replyroom.getFieldName(), (List) list);
    }

    public Iterator<String> getReplyTo() {
        return getFieldValues(ConfigureNodeFields.replyto);
    }

    public void setReplyTo(List<String> list) {
        addField(ConfigureNodeFields.replyto, FormField.TYPE_LIST_MULTI);
        setAnswer(ConfigureNodeFields.replyto.getFieldName(), (List) list);
    }

    public Iterator<String> getRosterGroupsAllowed() {
        return getFieldValues(ConfigureNodeFields.roster_groups_allowed);
    }

    public void setRosterGroupsAllowed(List<String> list) {
        addField(ConfigureNodeFields.roster_groups_allowed, FormField.TYPE_LIST_MULTI);
        setAnswer(ConfigureNodeFields.roster_groups_allowed.getFieldName(), (List) list);
    }

    public boolean isSubscibe() {
        return parseBoolean(getFieldValue(ConfigureNodeFields.subscribe));
    }

    public void setSubscribe(boolean z) {
        addField(ConfigureNodeFields.subscribe, FormField.TYPE_BOOLEAN);
        setAnswer(ConfigureNodeFields.subscribe.getFieldName(), z);
    }

    public String getTitle() {
        return getFieldValue(ConfigureNodeFields.title);
    }

    public void setTitle(String str) {
        addField(ConfigureNodeFields.title, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.title.getFieldName(), str);
    }

    public String getDataType() {
        return getFieldValue(ConfigureNodeFields.type);
    }

    public void setDataType(String str) {
        addField(ConfigureNodeFields.type, FormField.TYPE_TEXT_SINGLE);
        setAnswer(ConfigureNodeFields.type.getFieldName(), str);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(getClass().getName() + " Content [");
        Iterator fields = getFields();
        while (fields.hasNext()) {
            FormField formField = (FormField) fields.next();
            stringBuilder.append('(');
            stringBuilder.append(formField.getVariable());
            stringBuilder.append(':');
            Iterator values = formField.getValues();
            CharSequence stringBuilder2 = new StringBuilder();
            while (values.hasNext()) {
                if (stringBuilder2.length() > 0) {
                    stringBuilder.append(',');
                }
                stringBuilder2.append((String) values.next());
            }
            if (stringBuilder2.length() == 0) {
                stringBuilder2.append("NOT SET");
            }
            stringBuilder.append(stringBuilder2);
            stringBuilder.append(')');
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    private static boolean parseBoolean(String str) {
        return Contact.RELATION_FRIEND.equals(str) || "true".equals(str);
    }

    private String getFieldValue(ConfigureNodeFields configureNodeFields) {
        FormField field = getField(configureNodeFields.getFieldName());
        return field.getValues().hasNext() ? (String) field.getValues().next() : null;
    }

    private Iterator<String> getFieldValues(ConfigureNodeFields configureNodeFields) {
        return getField(configureNodeFields.getFieldName()).getValues();
    }

    private void addField(ConfigureNodeFields configureNodeFields, String str) {
        String fieldName = configureNodeFields.getFieldName();
        if (getField(fieldName) == null) {
            FormField formField = new FormField(fieldName);
            formField.setType(str);
            addField(formField);
        }
    }

    private List<String> getListSingle(String str) {
        List<String> arrayList = new ArrayList(1);
        arrayList.add(str);
        return arrayList;
    }
}
