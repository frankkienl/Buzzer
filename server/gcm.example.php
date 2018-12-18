<?php
/**
 * Created by PhpStorm.
 * User: frankb
 * Date: 15/11/2018
 * Time: 20:54
 */

$myKey = "EXAMPLE - INSERT YOUR OWN KEY HERE";
$deviceRegistrationId = $_GET['token'];

$headers = ["Content-Type:" . "application/json", "Authorization:" . "key=" . $myKey];
$data = [
    'data' => ['a' => 'a'],
    'priority' => 'high',
    'android' => ['priority' => 'high'],
    'registration_ids' => [$deviceRegistrationId]
];

$ch = curl_init();

curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_URL, "https://android.googleapis.com/gcm/send");
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
error_log(json_encode($data));
$response = curl_exec($ch);
curl_close($ch);
echo $response;
error_log($response);