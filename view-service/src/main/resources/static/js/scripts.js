var main = {
    init : function () {

        /* 초기화 섹션 */
        $('#div-choiceCust').hide();
        /* 끝 */

        var _this = this;
        $('#btn-add').on('click', function () {
            _this.add();
        });

        $('#btn-save').on('click', function () {
            _this.saveCust();
        });

        $('#btn-close').on('click', function() {
            _this.closeModal();
        });

        $('#btn-addPhoneNumber').on('click', function () {
            $('#div-addPhoneNumber').show();
            $('#div-choiceCust').hide();
        });

        $('#btn-choiceCust').on('click', function () {
            $('#div-addPhoneNumber').hide();
            $('#div-choiceCust').show();
//            console.log("고객 전체 조회 시작");
            oper.ajax('GET','','/api/custs',callback.choiceCust);
        });

        $('#btn-send').on('click', function () {
            _this.sendSms();
        });

        $('#phoneNumber').on('input', function() {
            $(this).val($(this).val().replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/\-{1,2}$/g, ""));
        });

        $('.cancel-booking').on('click', function () {
            _this.cancelBooking(this);
        });

        $('#placeholdersTable tbody').on('dblclick','tr', function () {
            _this.insertPlaceholder(this);
        });

        $('#selectItem').on('change', function() {
            _this.searchbooking();
        });

    },

    add : function () {
        var phonenumber = $('#ipt-phonenumber').val();
        if (oper.isEmpty(phonenumber)) {
            alert("전화번호를 입력해주세요.")

        } else {
            var insertTr = "";
            insertTr += "<tr><td>";
            insertTr += phonenumber;
            insertTr += "</td></tr>";
            $('tbody').append(insertTr);
            $('#ipt-phonenumber').val("");
        }

    },

    saveCust : function () {
        var data = {
            name: $('#name').val(),
            phoneNumber: $('#phonenumber').val().replace(/-/g,""),
            smsConsentType: $('#smsConsentType').val()
        }
        console.log("고객저장시작")
        oper.ajax("POST",data,'/api/custs', callback.saveCust);
    },

    sendSms : function () {
        let custIdList = [];
        $(".cust-checkbox:checked").each(function() {
            let custId = $(this).closest("tr").find("td").eq(1).text().trim();
            custIdList.push(custId);
        });

        let selectedRow = $(".template-radio:checked").closest("tr");

//        if (selectedRow.length === 0) {
//            alert("선택된 행이 없습니다.");
//            return;
//        }

        let template = {
            id: selectedRow.find("td").eq(1).text().trim(),
            smsType: selectedRow.find("td").eq(3).text().trim(),
        };

        console.log(selectedRow);
        console.log(template);
        console.log(oper.getTodayDt());

        var itemId = $("#selectItem").val();
        console.log(itemId);

        var data = {
                custIdList: custIdList,
                templateId: template.id,
                sendDt : oper.getTodayDt(),
                itemId : itemId
                }
        oper.ajax("POST",data,'/sms/send-by-cust-ids', callback.sendSms);
    },

    closeModal : function() {
        window.location.href='/cust/findAll';
    },

    cancelBooking : function (btn) {
        let bookingRow = $(btn).closest("tr");
        console.log(bookingRow);
        var bookingId = bookingRow.find("td").eq(0).text().trim();
        console.log(bookingId)
        var data = {}
        oper.ajax("POST",data,'/api/bookings/'+bookingId+'/cancel', callback.cancelBooking);
    },

    insertPlaceholder : function (tr) {
        var koText = $(tr).find("td").eq(2).text().trim();
        var curr = $('#templateContent').val();
        $('#templateContent').val(curr+"#{"+koText+"} ");
    },

    searchbooking : function () {
        var itemId = $("#selectItem").val();

        if(itemId){
             var data = {
                        'itemId' : itemId ,
                        'bookingStatus' : 'BOOK'
                       };
        }

        oper.ajax("GET", data, "/api/bookings/search", callback.searchbooking);
    }

};

var callback = {
    saveCust : function (data) {
        console.log("고객저장콜백시작");
        alert("고객 등록이 완료되었습니다");
        window.location.href = "/";
//        $("#frm-reset")[0].reset();
    } ,

    choiceCust : function (data) {
        console.log("고객 조회 완료");
        var tbody = $('#tbody');
        tbody.empty();
        data.forEach(cust => {
            var row = '<tr>'
            row+='<td><input type="checkbox" name="chb_cust" value="" </td>'
            row+='<td>'+cust.id+'</td>'
            row+='<td>'+cust.name+'</td>'
            row+='</tr>'
            tbody.append(row);
        });
    } ,

    sendSms : function () {
        alert("sms 발송이 완료되었습니다");
        window.location.href='/sms/send';
    } ,

    cancelBooking : function () {
        alert("예매가 취소 되었습니다");
        window.location.href='/bookings';
    } ,

    searchbooking : function (data) {
        var tbody = $('#custBody');
        tbody.empty();
        data.forEach(cust => {
            var row = '<tr>'
            row+='<td><input type="checkbox" class="cust-checkbox"> </td>'
            row+='<td>'+cust.custId+'</td>'
            row+='<td>'+cust.custName+'</td>'
            row+='</tr>'
            tbody.append(row);
        });
    }

}

var oper = {
    isEmpty : function (value) {
        if(value == "" || value == null || value == undefined) {
            return true;
        } else {
            return false;
        }
    },

    ajax : function (type, data, url, callback) {
        // GET이면 쿼리스트링으로 붙이고 data는 제거
        if (type === "GET" && data && Object.keys(data).length > 0) {
            const queryString = $.param(data); // itemId=123&bookingStatus=BOOK
            url += (url.includes("?") ? "&" : "?") + queryString;
            data = null;
        }

        $.ajax({
            'type': type,
            'url':url,
            'dataType':'json',
            'contentType':'application/json; charset=utf-8',
            'data': type === "GET" ? null : JSON.stringify(data)
        })
        .done(function (response){
            callback(response);
        })
        .fail(function(xhr, status, error) {
            // 요청이 실패했을 때 실행되는 코드
            console.error('요청 실패:', xhr.responseJSON);
            var errMsg = xhr.responseJSON.message;
            alert(errMsg);
        })
//        .always(function (){
//            console.log("ajax always 로그");
//        });
    },

    getTodayDt : function() {
        let today = new Date();
        let year = String(today.getFullYear());
        let month = String(today.getMonth()+1).padStart(2,"0");
        let date = String(today.getDate()).padStart(2,"0");
        let hours = String(today.getHours()).padStart(2,"0");
        let minutes = String(today.getMinutes()).padStart(2,"0");
        return year+month+date+hours+minutes;
    }
}

main.init();