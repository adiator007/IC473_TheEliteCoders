'use strict';

angular.module('webApp.test', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/test',{
		templateUrl: 'Test/test.html',
		controller: 'TestCtrl'
	});
}])


.controller('TestCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location', function($scope, CommonProp, $firebaseArray, $firebaseObject, $location){
	$scope.username = CommonProp.getUser();

	if(!$scope.username){
		$location.path('/home');
	}

    
    var ref = firebase.database().ref().child('Customers').orderByChild("kycDone").equalTo(false);
	$scope.customers = $firebaseArray(ref);	

    $scope.kycDocumentPage =function(id)
	{
		$scope.customersrsid =id;
		console.log("Consumer" + id);

		var ref = firebase.database().ref().child('CustomerKYC').child(id);
		$scope.kycdoc = $firebaseObject(ref);
		console.log("User kyc doc" +$scope.kycdoc.qrlink );

    };
 

    $scope.approveKyc = function(id)
    {
        var ref = firebase.database().ref().child('Customers').child(id);
            ref.update({
                accountStatus: "approved",
                kycDone: true
            }).then(function(ref){
                $scope.$apply(function(){
                    $("#Approvmodel").modal('hide');
                });
            }, function(error){
                console.log(error);
            });
        };


    $scope.rejectKyc = function(id)
    {
        var ref = firebase.database().ref().child('Customers').child(id);
            ref.update({
                accountStatus: "reject",
                kycDone: false
            }).then(function(ref){
                $scope.$apply(function(){
                    $("#rejectmodel").modal('hide');
                });
            }, function(error){
                console.log(error);
            });
        };

    
  


}])