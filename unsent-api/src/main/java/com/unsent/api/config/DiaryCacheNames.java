package com.unsent.api.config;

/**
 * Names of every cache holding diary reads. Any write to a diary entry
 * invalidates all of them, so they are kept together here.
 */
public final class DiaryCacheNames {

    public static final String ALL_ENTRIES = "GET-DIARIES-ENTRIES";
    public static final String ENTRY_BY_RECORD_ID = "GET-DIARIES-ENTRIES-BY-USER";
    public static final String ENTRIES_BY_USER = "diaryEntries";
    public static final String COUNT_BY_USER = "COUNT-DIARIES";
    public static final String SEARCH = "FILTER";

    private DiaryCacheNames() {
    }
}
