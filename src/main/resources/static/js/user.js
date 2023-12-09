let index={
   init: function(){
      $("#btn-save").on("click", ()=>{
          this.save();
       });
      $("#btn-update").on("click", ()=>{
          this.update();
       });
   },
   save: function(){
     //alert("user의 save함수 호출됨");
     let data={
       username:$("#username").val(),
       password:$("#password").val(),
       email:$("#email").val()
     };
     //console.log(data); javaScript -> Object
     // ajax통신을 이용해서 3개의 데이터를 json으로 변경해서 insert요청!
     // <회원가입시 Ajax를 사용하는 2가지 이유>
     // 1. 요청에 대한 응답을 html이 아닌 Data(json)를 받기 위하여!
     // 서버를 1개로 통일 가능
     // 2. 비동기 통신을 하기 위해서 이다.
     // ajax호출시 default가 비동기 호출
     $.ajax({
         type :"POST",
         url : "/auth/joinProc",
         data : JSON.stringify(data), //json문자열, http body데이터
         contentType:"application/json;charset=utf-8", // body데이터가 어떤 타입인지(MIME)
         dataType : "json" // 응답이 왔을 때 받는 데이터 타입이 json이면 => javsscript Object로 변경
     }).done(function(resp){
        if(resp.status==500){
          alert("회원가입이 실패되었습니다.");
        }else{
          alert("회원가입이 완료되었습니다.");
          //console.log(resp);
          location.href="/";
        }
     }).fail(function(error){
        alert(JSON.stringify(error));
     });
   },
   update: function(){
        //alert("user의 save함수 호출됨");
        let data={
          id : $("#id").val(),
          username:$("#username").val(),
          password:$("#password").val(),
          email:$("#email").val()
        };
        $.ajax({
            type :"PUT",
            url : "/user",
            data : JSON.stringify(data), //json문자열, http body데이터
            contentType:"application/json;charset=utf-8", // body데이터가 어떤 타입인지(MIME)
            dataType : "json" // 응답이 왔을 때 받는 데이터 타입이 json이면 => javsscript Object로 변경
        }).done(function(resp){
           alert("회원수정이 완료되었습니다.");
           //console.log(resp);
           location.href="/";
        }).fail(function(error){
           alert(JSON.stringify(error));
        });

      },

}
index.init();