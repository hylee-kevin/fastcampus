<html>
<body>
<div style="font-size:25px">
<?php
$host=gethostname();
echo "Container hostName & Port#: ";
echo $host;
print " - ";
echo exec('echo $SERVER_PORT');
?>
<p> Docker Load Balancer = (Nginx host) + (PHP & Apache contaier) </p>
</div>
</body>
</html>
