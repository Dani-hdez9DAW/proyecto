$(document).ready(function () {
    var $modal = $('.modal-frame');
    var $overlay = $('.modal-overlay');

    // Esta función maneja el final de la animación del modal
    $modal.on('webkitAnimationEnd oanimationend msAnimationEnd animationend', function (e) {
        if ($modal.hasClass('state-leave')) {
            $modal.removeClass('state-leave');
        }
    });

    // Esta función maneja el clic en el botón de cerrar
    $('.close').on('click', function () {
        $overlay.removeClass('state-show');
        $modal.removeClass('state-appear').addClass('state-leave');
    });

    // Esta función maneja el clic en el botón de abrir
    $('.open').on('click', function () {
        $overlay.addClass('state-show');
        $modal.removeClass('state-leave').addClass('state-appear');
    });

    // Esta función maneja el clic en los elementos con clase "modal-trigger"
    $(".modal-trigger").click(function (e) {
        e.preventDefault();
        var dataModal = $(this).attr("data-modal");
        $("#" + dataModal).css({ "display": "block" });
    });

    // Esta función maneja el clic en los elementos con clase "close-modal" y ".modal-sandbox"
    $(".close-modal, .modal-sandbox").click(function () {
        $(".modal").css({ "display": "none" });
    });
    $(document).ready(function () {
        function updateCircle(val, circleId, contId, maxScale) {
            var $circle = $(circleId);
            var $text = $(contId + ' text');
    
            if (isNaN(val)) {
                val = maxScale;
            } else {
                var r = $circle.attr('r');
                var c = Math.PI * (r * 2);
    
                if (val < 0) { val = 0; }
                if (val > maxScale) { val = maxScale; }
    
                var pct = ((maxScale - val) / maxScale) * c;
    
                $circle.css({ strokeDashoffset: pct });
                $(contId).attr('data-pct', val);
                $text.text(val);
            }
        }
    
        var val1 = parseInt($('#puntuacion1').val());
        updateCircle(val1, '#svg1 #bar1', '#cont1', 10);
    
        var val2 = parseInt($('#puntuacion2').val());
        updateCircle(val2, '#svg2 #bar2', '#cont2', 5);
    });
});
