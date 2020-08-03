
var app = angular.module('CustomServices', []); 
app.factory('distributeridservice', function() {
    var distributerId ={};
    function set(data) {
      distributerId = data;
    }
    function get() {
     return distributerId;
    }
   
    return {
     set: set,
     get: get
    }
   
   });