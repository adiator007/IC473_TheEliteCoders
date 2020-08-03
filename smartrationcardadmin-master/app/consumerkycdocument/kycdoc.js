'use strict';

angular.module('webApp.kycdocconsumer', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/kycdocconsumer',{
		templateUrl: 'consumerkycdocument/kycdoc.html',
		controller: 'ConkycCtrl'
	});
}])

.controller('ConkycCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location' , 'distributeridservice' , function($scope, CommonProp, $firebaseArray, $firebaseObject, $location ,distributeridservice ){
	$scope.username = CommonProp.getUser();

	

    $scope.init = function(id) {
        $scope.distributersId = id;
		console.log("Inside Init: " + $scope.distributersId);
	
    
    }
	if(!$scope.username){
		$location.path('/home');
	}

			
	var ref = firebase.database().ref().child('CustomerKYC').child(distributeridservice.get()).child('profilepic');
	$scope.kycdoc = $firebaseObject(ref);	
	

    var adharCardRef = firebase.database().ref().child('CustomerKYC').child(distributeridservice.get()).child('aadharcard');
    $scope.adahrcard = $firebaseObject(adharCardRef);	
    
    var RationCardRef = firebase.database().ref().child('CustomerKYC').child(distributeridservice.get()).child('pancard');
    $scope.rationCard = $firebaseObject(RationCardRef);	
    
    var ProfilePicRef = firebase.database().ref().child('CustomerKYC').child(distributeridservice.get()).child('profilepic');
	$scope.profilepic = $firebaseObject(ProfilePicRef);	

	console.log("distributerid " + distributeridservice.get());
	console.log("Inside Init: " + $scope.kycdoc.url);


    // var rationTypeRef = firebase.database().ref().child('ConsumerRationType').child(distributeridservice.get());
    $scope.rationType = "";	



	$scope.approveKyc = function(id)
    {
        var ref = firebase.database().ref().child('Customers').child(distributeridservice.get());
        var rationTypeRef = firebase.database().ref().child('ConsumerRationType').child(distributeridservice.get());


        console.log("ConsumerID " + distributeridservice.get());
        console.log("cardType " + $scope.rationType.type);
       
        rationTypeRef.update({
            consumerid:distributeridservice.get(),
            cardtype:   $scope.rationType.type
        }).then(function(rationTypeRef){

            
            if(angular.equals($scope.rationType.type, "white")){

                
                        ref.update({
                            accountStatus: "approved",
                            kycDone: "yes"  ,
                            walletAmmount: 200
                        }).then(function(ref){


                            $scope.$apply(function(){
                                $location.path('/consumers');
                            });
                        }, function(error){
                            console.log(error);
                        });
                    }

                    else if(angular.equals($scope.rationType.type, "orange")){
    
                
                        ref.update({
                            accountStatus: "approved",
                            kycDone: "yes"  ,
                            walletAmmount: 300
                        }).then(function(ref){
                    
                    
                            $scope.$apply(function(){
                                $location.path('/consumers');
                            });
                        }, function(error){
                            console.log(error);
                        });
                    }
                    
                   else if(angular.equals($scope.rationType.type, "yellow")){
                    
                                    
                        ref.update({
                            accountStatus: "approved",
                            kycDone: "yes"  ,
                            walletAmmount: 400
                        }).then(function(ref){
                    
                    
                            $scope.$apply(function(){
                                $location.path('/consumers');
                            });
                        }, function(error){
                            console.log(error);
                        });
                    }
                    


           
        });

        };


        $scope.rejectKyc = function(id)
        {
            var ref = firebase.database().ref().child('Customers').child(distributeridservice.get());
                ref.update({
                    accountStatus: "reject",
                    kycDone: "no"
                }).then(function(ref){
                    $scope.$apply(function(){
                        $location.path('/consumers');
                    });
                }, function(error){
                    console.log(error);
                });
            };
    

    
}])
