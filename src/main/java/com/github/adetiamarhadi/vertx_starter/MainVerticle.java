package com.github.adetiamarhadi.vertx_starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> promise) {

    Promise<String> dbVerticleDeployment = Promise.promise();

    vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment);

    dbVerticleDeployment.future().compose(id -> {

      Promise<String> httpVerticleDeployment = Promise.promise();

      vertx.deployVerticle("com.github.adetiamarhadi.vertx_starter.HttpServerVerticle",
        new DeploymentOptions().setInstances(2), httpVerticleDeployment);

      return httpVerticleDeployment.future();
    }).onComplete(ar -> {

      if (ar.succeeded()) {
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });
  }

}
