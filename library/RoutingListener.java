package anulom.executioner.com3.anulom.library;

import java.util.List;

public interface RoutingListener {
    void onRoutingFailure(RouteException e);
    void onRoutingSuccess(List<Route> route, int shortestRouteIndex);
          void onRoutingCancelled();
          void onRoutingStart();

}
