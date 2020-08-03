'use strict';

angular.module('webApp.kycdoc', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/kycdoc',{
		templateUrl: 'distributerkycdocument/kycdoc.html',
		controller: 'kycCtrl'
	});
}])

.controller('kycCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location' , 'distributeridservice' , function($scope, CommonProp, $firebaseArray, $firebaseObject, $location ,distributeridservice ){
	$scope.username = CommonProp.getUser();

	

    $scope.init = function(id) {
        $scope.distributersId = id;
		console.log("Inside Init: " + $scope.distributersId);
	
    
    }
	if(!$scope.username){
		$location.path('/home');
	}

			
	var ref = firebase.database().ref().child('DistributorKYC').child(distributeridservice.get());
    $scope.kycdoc = $firebaseObject(ref);	
    
    var adharcardRef = firebase.database().ref().child('DistributorKYC').child(distributeridservice.get()).child("aadharcard");
    $scope.adharcard = $firebaseObject(adharcardRef);
    
    
    var pancardRef = firebase.database().ref().child('DistributorKYC').child(distributeridservice.get()).child("aadharcard");
    $scope.pancard = $firebaseObject(pancardRef);


    var shoplicenceRef = firebase.database().ref().child('DistributorKYC').child(distributeridservice.get()).child("aadharcard");
    $scope.shoplicence = $firebaseObject(shoplicenceRef);

	console.log("distributerid " + distributeridservice.get());
	console.log("Inside Init: " + $scope.kycdoc.qrlink);



	$scope.approveKyc = function(id)
    {
        var ref = firebase.database().ref().child('Distributors').child(distributeridservice.get());
            ref.update({
                accountStatus: "approved",
                kycDone: "yes"
            }).then(function(ref){
                $scope.$apply(function(){
					$location.path('/distributers');
                });
            }, function(error){
                console.log(error);
            });
        };


        $scope.rejectKyc = function(id)
        {
            var ref = firebase.database().ref().child('Distributors').child(distributeridservice.get());
                ref.update({
                    accountStatus: "reject",
                    kycDone: "no"
                }).then(function(ref){
                    $scope.$apply(function(){
                        $location.path('/distributers');
                    });
                }, function(error){
                    console.log(error);
                });
            };
    

    
}])
