(define (reverse lst)
  (let loop ((result '()) (lst lst))
    (if (null? lst)
        result
        (loop (cons (car lst) result) (cdr lst)))))

(define (deep-reverse lst)
  (let loop ((result '()) (lst lst))
    (if (null? lst)
        result
        (let* ((the-car (car lst))
               (elt (if (pair? the-car) (deep-reverse the-car) the-car)))
          (loop (cons elt result) (cdr lst))))))
