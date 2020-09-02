package net.mrporky.anisoc.util;

import java.util.List;

// Data class for JSON deserialisation of config file

public class Channel {
    private Long channelId;
    private List<RoleReactPair> roleReactPairs;

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public List<RoleReactPair> getRoleReactPairs() {
        return roleReactPairs;
    }

    public void setRoleReactPairs(List<RoleReactPair> roleReactPairs) {
        this.roleReactPairs = roleReactPairs;
    }
}
