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
        var val = parseInt($('#puntuacion').val());
        var $circle = $('#svg #bar');

        if (isNaN(val)) {
            val = 10;
        } else {
            var r = $circle.attr('r');
            var c = Math.PI * (r * 2);

            if (val < 0) { val = 0; }
            if (val > 10) { val = 10; }

            var pct = ((10 - val) / 10) * c;

            $circle.css({ strokeDashoffset: pct });
            $('#cont').attr('data-pct', val);
        }
    });
});