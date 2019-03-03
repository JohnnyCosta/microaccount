open module org.micro.app.order {
  requires vertx.core;
  requires vertx.web;
  requires vertx.web.client;
  requires vertx.config;
  requires vertx.service.discovery.bridge.consul;
  requires vertx.service.discovery;
  requires org.micro.domain;
  requires org.micro.config;
  requires org.micro.controller;
  requires jackson.annotations;

  requires static lombok;

}
