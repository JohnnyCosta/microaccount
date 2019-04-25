open module org.micro.app.account {
  requires vertx.core;
  requires vertx.web;
  requires vertx.web.client;
  requires vertx.config;
  requires vertx.health.check;
  requires vertx.service.discovery;
  requires vertx.service.discovery.bridge.consul;
  requires org.micro.config;
  requires org.micro.controller;
  requires jackson.annotations;

  requires static lombok;

}
