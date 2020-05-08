package com.nirima.jenkins.plugins.docker.cloudstat;

import io.jenkins.docker.DockerComputer;
import io.jenkins.docker.DockerTransientNode;
import org.jenkinsci.plugins.cloudstats.CloudStatistics;
import org.jenkinsci.plugins.cloudstats.ProvisioningActivity;
import org.jenkinsci.plugins.cloudstats.TrackedItem;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

@Restricted(NoExternalUse.class)
class TrackedDockerComputer extends DockerComputer implements TrackedItem {

    private final ProvisioningActivity.Id id;

    public TrackedDockerComputer(@Nonnull  DockerTransientNode node, @Nonnull ProvisioningActivity.Id id) {
        super(node);
        this.id = id;
    }


    @Nullable
    @Override
    public ProvisioningActivity.Id getId() {
        return id;
    }

    @Override
    public void recordTermination() {
        try {
            super.recordTermination();
        } finally {
            _terminate();
        }
    }

    private void _terminate(){
        ProvisioningActivity activity = CloudStatistics.get().getActivityFor(this);
        if (nonNull(activity)) {
            activity.enterIfNotAlready(ProvisioningActivity.Phase.COMPLETED);
        }
    }
}