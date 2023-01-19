package com.virtuslab.gitmachete.frontend.ui.services;

import javax.swing.JComponent;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import git4idea.repo.GitRepository;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.virtuslab.gitmachete.frontend.ui.api.gitrepositoryselection.IGitRepositorySelectionChangeObserver;
import com.virtuslab.gitmachete.frontend.ui.api.gitrepositoryselection.IGitRepositorySelectionProvider;

@Service
public final class SelectedGitRepositoryService implements IGitRepositorySelectionProvider {

  private final IGitRepositorySelectionProvider selectionComponent;

  public SelectedGitRepositoryService(Project project) {
    this.selectionComponent = project.getService(IGitRepositorySelectionProvider.class);
  }

  @Override
  public @Nullable GitRepository getSelectedGitRepository() {
    return selectionComponent.getSelectedGitRepository();
  }

  @Override
  public void addSelectionChangeObserver(IGitRepositorySelectionChangeObserver observer) {
    selectionComponent.addSelectionChangeObserver(observer);
  }

  @Override
  public JComponent getSelectionComponent() {
    return selectionComponent.getSelectionComponent();
  }

  public IGitRepositorySelectionProvider getGitRepositorySelectionProvider() {
    return selectionComponent;
  }
}
