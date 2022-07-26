// (1) 회원정보 수정
function update(userId, event) {
    event.preventDefault(); // 폼태그 액션 막기

    let data = $('#profileUpdate').serialize();

    console.log(data);

    $.ajax({

        type: 'PUT',
        url: `/api/user/${userId}`,
        data: data,
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        dataType: 'json'

    }).done(res => { // HttpStatus 상태코드 200번대
        console.log('성공', res);
        location.href = `/user/${userId}`;
    }).fail(e => { // HttpStatus 상태코드 200번대가 아 닐 때
        if (e.data == null) {
            alert(e.responseJSON.message);
        } else {
            alert(JSON.stringify(e.responseJSON.data.name));
        }
        
        console.log('실패', e);
    });
    
}