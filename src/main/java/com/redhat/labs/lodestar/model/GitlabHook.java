package com.redhat.labs.lodestar.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GitlabHook {
    private String name;
    private String baseUrl;
    private boolean pushEvent;
    private String pushEventsBranchFilter;
    private String token;

    /**
     * Should the webhook be enabled after an engagement is archived
     */
    private boolean enabledAfterArchive;

}
