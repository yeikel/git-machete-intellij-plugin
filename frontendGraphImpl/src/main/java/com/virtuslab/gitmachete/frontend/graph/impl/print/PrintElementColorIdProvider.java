package com.virtuslab.gitmachete.frontend.graph.impl.print;

import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

import com.virtuslab.gitmachete.frontend.graph.api.elements.IGraphElement;
import com.virtuslab.gitmachete.frontend.graph.api.items.IGraphItem;
import com.virtuslab.gitmachete.frontend.graph.api.print.IPrintElementColorIdProvider;
import com.virtuslab.gitmachete.frontend.graph.api.repository.IRepositoryGraph;

public class PrintElementColorIdProvider implements IPrintElementColorIdProvider {
  @NotOnlyInitialized
  private final IRepositoryGraph repositoryGraph;

  public PrintElementColorIdProvider(@UnderInitialization IRepositoryGraph repositoryGraph) {
    this.repositoryGraph = repositoryGraph;
  }

  public int getColorId(IGraphElement element) {
    int nodeIndex;
    if (element.isNode()) {
      nodeIndex = element.asNode().getNodeIndex();
    } else { // isEdge
      nodeIndex = element.asEdge().getDownNodeIndex();
    }

    IGraphItem graphItem = repositoryGraph.getGraphItem(nodeIndex);

    return graphItem.getGraphItemColor().getId();
  }
}
