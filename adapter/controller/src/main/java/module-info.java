module org.micro.controller {
  exports org.micro.controller;
  exports org.micro.vm;
  exports org.micro.client;
  requires static lombok;
  requires org.micro.domain;
  requires org.micro.scenario;
  requires vertx.web;
  requires vertx.web.client;
  requires vertx.service.discovery;
  requires vertx.core;
  requires jackson.annotations;
}
