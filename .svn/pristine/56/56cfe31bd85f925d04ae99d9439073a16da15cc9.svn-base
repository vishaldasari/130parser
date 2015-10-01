;; Product using Continuation Passing Style
(define (product ls k)
  (let ((break k))
    (define (f ls k)
      (cond
        ((null? ls) (k 1))
        ((= (car ls) 0) (break 0))
        (else (f (cdr ls)
                 (lambda (x)
                   (k (* (car ls) x)))))))
    (f ls k)))

(product '(1 2 3 4 5) display)
(newline)
(product '(7 3 8 0 19 5) display)
(newline)
