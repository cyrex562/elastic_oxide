/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.core.security.authc.pki;

import org.elasticsearch.common.settings.SecureString;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.xpack.core.security.authc.RealmSettings;
import org.elasticsearch.xpack.core.security.authc.support.DelegatedAuthorizationSettings;
import org.elasticsearch.xpack.core.security.authc.support.mapper.CompositeRoleMapperSettings;
import org.elasticsearch.xpack.core.ssl.SSLConfigurationSettings;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public final class PkiRealmSettings {
    public static final String TYPE = "pki";
    public static final String DEFAULT_USERNAME_PATTERN = "CN=(.*?)(?:,|$)";
    public static final Setting.AffixSetting<Pattern> USERNAME_PATTERN_SETTING = Setting.affixKeySetting(
            RealmSettings.realmSettingPrefix(TYPE), "username_pattern",
            key -> new Setting<>(key, DEFAULT_USERNAME_PATTERN, s -> Pattern.compile(s, Pattern.CASE_INSENSITIVE),
                    Setting.Property.NodeScope));

    private static final TimeValue DEFAULT_TTL = TimeValue.timeValueMinutes(20);
    public static final Setting.AffixSetting<TimeValue> CACHE_TTL_SETTING = Setting.affixKeySetting(
        RealmSettings.realmSettingPrefix(TYPE), "cache.ttl",
        key -> Setting.timeSetting(key, DEFAULT_TTL, Setting.Property.NodeScope));

    private static final int DEFAULT_MAX_USERS = 100_000; //100k users
    public static final Setting.AffixSetting<Integer> CACHE_MAX_USERS_SETTING = Setting.affixKeySetting(
        RealmSettings.realmSettingPrefix(TYPE), "cache.max_users",
        key -> Setting.intSetting(key, DEFAULT_MAX_USERS, Setting.Property.NodeScope));

    public static final Setting.AffixSetting<Boolean> DELEGATION_ENABLED_SETTING = Setting.affixKeySetting(
            RealmSettings.realmSettingPrefix(TYPE), "delegation.enabled",
            key -> Setting.boolSetting(key, false, Setting.Property.NodeScope));

    public static final Setting.AffixSetting<Optional<String>> TRUST_STORE_PATH;
    public static final Setting.AffixSetting<Optional<String>> TRUST_STORE_TYPE;
    public static final Setting.AffixSetting<SecureString> TRUST_STORE_PASSWORD;
    public static final Setting.AffixSetting<SecureString> LEGACY_TRUST_STORE_PASSWORD;
    public static final Setting.AffixSetting<String> TRUST_STORE_ALGORITHM;
    public static final Setting.AffixSetting<List<String>> CAPATH_SETTING;

    static {
        final String prefix = "xpack.security.authc.realms." + TYPE + ".";
        // Can't use the "realm" settings because they have the format "ssl.{setting}" which isn't the setting format for PKI trust
        TRUST_STORE_PATH = SSLConfigurationSettings.TRUSTSTORE_PATH.affixSetting(prefix, "");
        TRUST_STORE_TYPE = SSLConfigurationSettings.TRUSTSTORE_TYPE.affixSetting(prefix, "");
        TRUST_STORE_PASSWORD = SSLConfigurationSettings.TRUSTSTORE_PASSWORD.affixSetting(prefix, "");
        LEGACY_TRUST_STORE_PASSWORD = SSLConfigurationSettings.LEGACY_TRUSTSTORE_PASSWORD.affixSetting(prefix, "");
        TRUST_STORE_ALGORITHM = SSLConfigurationSettings.TRUSTSTORE_ALGORITHM.affixSetting(prefix, "");
        CAPATH_SETTING = SSLConfigurationSettings.CERT_AUTH_PATH.affixSetting(prefix, "");
    }

    private PkiRealmSettings() {
    }

    /**
     * @return The {@link Setting setting configuration} for this realm type
     */
    public static Set<Setting.AffixSetting<?>> getSettings() {
        Set<Setting.AffixSetting<?>> settings = new HashSet<>();
        settings.add(USERNAME_PATTERN_SETTING);
        settings.add(CACHE_TTL_SETTING);
        settings.add(CACHE_MAX_USERS_SETTING);
        settings.add(DELEGATION_ENABLED_SETTING);

        settings.add(TRUST_STORE_PATH);
        settings.add(TRUST_STORE_PASSWORD);
        settings.add(LEGACY_TRUST_STORE_PASSWORD);
        settings.add(TRUST_STORE_ALGORITHM);
        settings.add(CAPATH_SETTING);

        settings.addAll(DelegatedAuthorizationSettings.getSettings(TYPE));
        settings.addAll(CompositeRoleMapperSettings.getSettings(TYPE));

        settings.addAll(RealmSettings.getStandardSettings(TYPE));
        return settings;
    }
}
