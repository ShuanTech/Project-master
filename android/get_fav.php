<?php 
require('config.php');
if(isset($_POST['u_id'])){
	
	$sql=select_query("SELECT l.u_id,l.full_name,l.pro_pic,l.level FROM `login` l,
	favorite f WHERE l.u_id=f.to_id and 
	f.u_id='".$_POST['u_id']."'");
	
	$count=count($sql);
	$response['favorite']=array();
		if($count==""){
			$response["message"]="No Data";
			$response["success"]=0;
			echo json_encode($response);
		}else{
			for($i=0;$i<$count;$i++){
				array_push($response["favorite"],$sql[$i]);
			}
			$response['success']=1;
			echo json_encode($response); 
		} 
	
 }else{
	$response['msg']='No data post';
	$response['success']=0;
	echo json_encode($response);	
} 

function select_query($qry){
	
	try{
		require('config.php');
		
		$parse_qry=mysql_query($qry,$con);
		
		if(!$parse_qry){
			 die('Could not get data: ' . mysql_error());
		} 
		$res_qry=array();
		while(($row = mysql_fetch_array($parse_qry,MYSQL_ASSOC))!=false){
			$res_qry[]=$row;
		}
		return $res_qry;
		mysql_close($con);
	}catch(Exception $e){}
	
	
}

?>