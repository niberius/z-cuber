<html>
<head>
<script language="javascript">
var base = 60;
var clocktimer,dateObj,dh,dm,ds,ms;
var readout='';
var h=1;
var m=1;
var tm=1;
var s=0;
var ts=0;
var ms=0;
var show=true;
var init=0;
var ii=0;
var chrono = 15;
var startTimer=false;
document.onkeyup = findTIME;

function clearALL() {
	clearTimeout(clocktimer);
	h=1;m=1;tm=1;s=0;ts=0;ms=0;
	init=0;show=true;
	readout='00:00:00.00';
	document.clockform.clock.value=readout;
	var CF = document.clockform;
	ii = 0; 
    init=0;
	}

function startTIME() {
	var cdateObj = new Date();
	var t = (cdateObj.getTime() - dateObj.getTime())-(s*1000);
	if (t>999) {s++;}
	if (s>=(m*base)) {
		ts=0;
		m++; 
	} else{
		ts=parseInt((ms/100)+s);
		if(ts>=base) {
			ts=ts-((m-1)*base);
		} 
	}
	if (m>(h*base)) {
		tm=1;
		h++; 
	} else {
		tm=parseInt((ms/100)+m);
		if(tm>=base) {
			tm=tm-((h-1)*base);
		} 
	}
	ms = Math.round(t/10);
	if (ms>99) {ms=0;}
	if (ms==0) {ms='00';}
	if (ms>0 && ms <=9) { ms = '0'+ms; }
	if (ts>0) { 
		ds = ts; 
		if (ts < 10) { 
			ds = '0'+ts; 
		}
	} else { ds = '00'; }
	dm=tm-1;
	if (dm>0) {if(dm<10)
    {dm = '0' + dm; }} else { dm = '00'; }
	dh=h-1;
	if (dh>0) { if (dh< 10) { dh = '0'+dh; }} else { dh = '00'; }
	readout = dh + ':' + dm + ':' + ds + '.' + ms;
	if (show==true) { document.clockform.clock.value = readout; }
    if(init!=2){clocktimer = setTimeout("startTIME()",1); }    
	}
	
	function findTIME(e) {
    var KeyID = (window.event) ? event.keyCode : e.keyCode;
    if(KeyID==32){
			if (init==0) { 
				dateObj = new Date();
				startTIME();
				init=1;
				} else { init=2;} 	
        }
	}
	
	function getTime() {
		document.clockform.time.value = document.clockform.clock.value;
	}
	
	function cronometr15sec() {
    	startTimer=true;    	
        if(chrono == 0){
        	chrono=0;
            return 0;
        }
      	chrono = chrono-1;
        document.clockform.timer.value=chrono.toString();			
        setTimeout("cronometr15sec()",1000);
	}
	
	function createNewScramble() {
		var scramble  = "";
		for(var i = 0; i < 25;i++){
			var elem = Math.floor(Math.random()*24)
	    	switch(elem){
	    		case 0:	
                	scramble+="F ";
                    break;
	    		case 1:
	    			scramble+="B ";
	    			break;
	    		case 2:
	    			scramble+="L ";
	    			break;
	    		case 3:
	    			scramble+="R ";
	    			break;
	    		case 4:
	    			scramble+="U ";
	    			break;
	    		case 5:
	    			scramble+="D ";
	    			break;
	    		case 6:
	    			scramble+="F' ";
	    			break;
	    		case 7:
	    			scramble+="B' ";
	    			break;
	    		case 8:
	    			scramble+="L' ";
	    			break;
	    		case 9:
	    			scramble+="R' ";
	    			break;
	    		case 10:
	    			scramble+="U' ";
	    			break;
	    		case 11:
	    			scramble+="D' ";
	    			break;
	    		case 12:
	    			scramble+="F2 ";
	    			break;
	    		case 13:
	    			scramble+="B2 ";
	    			break;
	    		case 14:
	    			scramble+="L2 ";
	    			break;
	    		case 15:
	    			scramble+="R2 ";
	    			break;
	    		case 16:
	    			scramble+="U2 ";
	    			break;
	    		case 17:
	    			scramble+="D2 ";
	    			break;
	    		case 18:
	    			scramble+="F2' ";
	    			break;
	    		case 19:
	    			scramble+="B2' ";
	    			break;
	    		case 20:
	    			scramble+="L2' ";
	    			break;
	    		case 21:
	    			scramble+="R2' ";
	    			break;
	    		case 22:
	    			scramble+="U2' ";
	    			break;
	    		case 23:
	    			scramble+="D2' ";
	    			break;
	    		default:
	    			scramble+="";
	    			break;
	    	}	    	
		}
		document.clockform.newScramble.value = scramble;
	}
</script>

</head>
	<body>
		<form name=clockform> 
			<input id=scramble type=button value="New Scramble" onclick="createNewScramble()">
			<label>Scramble:<input name=newScramble type=text value="" style="font-size:13px;width: 812px; height: 24px; border:1px"></label>
			<input name=clearer type="button" value="Reset"onclick="clearALL()" style="font-size:15px; width: 85px"> 
			<input name=clock size=10 value="00:00:00.00" style="font-size:13px; width: 80px; height: 24px; border:1px solid #000000"> 
			<input name=save type="button" value="Save" onclick="getTime()">
            <input name=micro type="button" value="Microphone" onclick="cronometr15sec();this.style.display='none';">
            <input name=timer type=text size=10 value="" style="font-size:13px;"> 
			<p><label>Time:<input name=time type=text value=""></label></p>
		</form>
	</body>
</html>
