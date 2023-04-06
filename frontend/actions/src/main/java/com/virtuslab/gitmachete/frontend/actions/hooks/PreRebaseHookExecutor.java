package com.virtuslab.gitmachete.frontend.actions.hooks;

import git4idea.repo.GitRepository;
import io.vavr.collection.HashMap;
import kr.pe.kwonnam.slf4jlambda.LambdaLogger;
import lombok.CustomLog;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.virtuslab.gitmachete.backend.api.GitMacheteException;
import com.virtuslab.gitmachete.backend.api.IGitRebaseParameters;
import com.virtuslab.gitmachete.backend.hooks.ExecutionResult;
import com.virtuslab.gitmachete.backend.hooks.OnExecutionTimeout;
import com.virtuslab.qual.guieffect.UIThreadUnsafe;

@CustomLog
public final class PreRebaseHookExecutor extends BaseGit4IdeaHookExecutor {
  private static final int EXECUTION_TIMEOUT_SECONDS = 10;

  @UIThreadUnsafe
  public PreRebaseHookExecutor(GitRepository gitRepository) {
    super("machete-pre-rebase", gitRepository);
  }

  /**
   * @param gitRebaseParameters git rebase parameters
   * @return an exit code (possibly non-zero) when the hook has been successfully executed,
   *         or null when the hook has not been executed (because it's absent or non-executable)
   * @throws GitMacheteException when a timeout or I/O exception occurs
   */
  @UIThreadUnsafe
  public @Nullable ExecutionResult executeHookFor(IGitRebaseParameters gitRebaseParameters) throws GitMacheteException {
    return executeHook(EXECUTION_TIMEOUT_SECONDS, OnExecutionTimeout.THROW,
        /* environment */ HashMap.empty(),
        gitRebaseParameters.getNewBaseBranch().getFullName(),
        gitRebaseParameters.getForkPointCommit().getHash(),
        gitRebaseParameters.getCurrentBranch().getName());
  }

  @Override
  protected LambdaLogger log() {
    return LOG;
  }
}