(define (same-parity first . rest)
  (let iter ([lst (cons first rest)])
    (cond
      [(null? lst) '()]
      [(equal? (even? first) (even? (car lst)))
       (cons (car lst) (iter (cdr lst)))]
      [else (iter (cdr lst))])))
