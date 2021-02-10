package fr.heavenmoon.core.common.utils.builders.motd;

import fr.heavenmoon.core.common.format.FormatUtils;

public class MotdBuilder {

    private Integer protocolId;
    private String protocolValue;
    private String description;


    public MotdBuilder(Integer protocolId, String protocolValue, String line1, String line2) {
        this.protocolId = protocolId;
        this.protocolValue = protocolValue;
        description = (FormatUtils.center(line1) + "\n" + line2);
    }

    public MotdBuilder(Integer protocolId, String protocolValue, String description) {
        this.protocolId = protocolId;
        this.protocolValue = protocolValue;
        this.description = description;
    }

    public Integer getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }

    public String getProtocolValue() {
        return protocolValue;
    }

    public void setProtocolValue(String protocolValue) {
        this.protocolValue = protocolValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

