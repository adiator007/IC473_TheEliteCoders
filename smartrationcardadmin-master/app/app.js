'use strict';

// Declare app level module which depends on views, and components
angular.module('webApp', [
  'ngRoute',
  'webApp.home',
  'webApp.register',
  'webApp.welcome',
  'webApp.addPost',
  'webApp.kycdoc' ,
    'webApp.test',
    'webApp.consumers',
  'webApp.distributers',
  'CustomServices' ,
  'webApp.kycdocconsumer' ,
  'webApp.approveddistributers' ,
  'webApp.approvedconsumer' ,
 'webApp.comment'
]).

config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {

  $routeProvider.otherwise({redirectTo: '/home'});
}]);

