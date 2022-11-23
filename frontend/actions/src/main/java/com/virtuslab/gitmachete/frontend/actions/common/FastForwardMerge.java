package com.virtuslab.gitmachete.frontend.actions.common;

import static com.virtuslab.gitmachete.frontend.actions.backgroundables.FetchBackgroundable.LOCAL_REPOSITORY_NAME;
import static com.virtuslab.gitmachete.frontend.actions.common.ActionUtils.createRefspec;
import static com.virtuslab.gitmachete.frontend.resourcebundles.GitMacheteBundle.getNonHtmlString;
import static com.virtuslab.gitmachete.frontend.resourcebundles.GitMacheteBundle.getString;

import git4idea.GitReference;
import git4idea.repo.GitRepository;
import io.vavr.control.Option;
import lombok.experimental.ExtensionMethod;
import lombok.val;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.tainting.qual.Untainted;

import com.virtuslab.gitmachete.frontend.actions.backgroundables.CheckRemoteBranchBackgroundable;
import com.virtuslab.gitmachete.frontend.actions.backgroundables.FetchBackgroundable;
import com.virtuslab.gitmachete.frontend.actions.backgroundables.MergeCurrentBranchFastForwardOnlyBackgroundable;
import com.virtuslab.gitmachete.frontend.resourcebundles.GitMacheteBundle;

@ExtensionMethod(GitMacheteBundle.class)
public final class FastForwardMerge {

  private FastForwardMerge() {}

  public static CheckRemoteBranchBackgroundable createBackgroundable(
      GitRepository gitRepository,
      MergeProps mergeProps,
      @Untainted String fetchNotificationTextPrefix) {
    val stayingName = mergeProps.getStayingBranch().getName();
    val currentBranchName = Option.of(gitRepository.getCurrentBranch()).map(GitReference::getName).getOrNull();
    val failFfMergeNotificationTitle = getString(
        "action.GitMachete.BaseFastForwardMergeToParentAction.notification.title.ff-fail");
    return new CheckRemoteBranchBackgroundable(gitRepository, stayingName, failFfMergeNotificationTitle,
        fetchNotificationTextPrefix) {
      @Override
      @UIEffect
      public void onSuccess() {
        if (mergeProps.getMovingBranch().getName().equals(currentBranchName)) {
          mergeCurrentBranch(gitRepository, mergeProps);
        } else {
          mergeNonCurrentBranch(gitRepository, mergeProps, fetchNotificationTextPrefix);
        }
      }
    };
  }

  private static void mergeCurrentBranch(GitRepository gitRepository, MergeProps mergeProps) {
    new MergeCurrentBranchFastForwardOnlyBackgroundable(gitRepository, mergeProps.getStayingBranch()).queue();
  }

  private static void mergeNonCurrentBranch(
      GitRepository gitRepository,
      MergeProps mergeProps,
      @Untainted String fetchNotificationTextPrefix) {
    val stayingFullName = mergeProps.getStayingBranch().getFullName();
    val movingFullName = mergeProps.getMovingBranch().getFullName();
    val refspecFromChildToParent = createRefspec(stayingFullName, movingFullName, /* allowNonFastForward */ false);
    val stayingName = mergeProps.getStayingBranch().getName();
    val movingName = mergeProps.getMovingBranch().getName();
    val successFFMergeNotification = getString(
        "action.GitMachete.BaseFastForwardMergeToParentAction.notification.text.ff-success").fmt(stayingName, movingName);
    val failFFMergeNotification = getNonHtmlString(
        "action.GitMachete.BaseFastForwardMergeToParentAction.notification.text.ff-fail").fmt(stayingName, movingName);
    new FetchBackgroundable(
        gitRepository,
        LOCAL_REPOSITORY_NAME,
        refspecFromChildToParent,
        getString("action.GitMachete.BaseFastForwardMergeToParentAction.task-title"),
        fetchNotificationTextPrefix + failFFMergeNotification,
        fetchNotificationTextPrefix + successFFMergeNotification).queue();
  }
}
