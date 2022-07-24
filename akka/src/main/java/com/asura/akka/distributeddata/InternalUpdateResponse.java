package com.asura.akka.distributeddata;

import akka.cluster.ddata.LWWMap;
import akka.cluster.ddata.typed.javadsl.Replicator;

/**
 * @author asura7969
 * @create 2022-07-24-22:39
 */
public class InternalUpdateResponse implements InternalCommand {
    public final Replicator.UpdateResponse<LWWMap<String, String>> rsp;

    public InternalUpdateResponse(Replicator.UpdateResponse<LWWMap<String, String>> rsp) {
        this.rsp = rsp;
    }
}
