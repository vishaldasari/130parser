(define zero (lambda (s) (lambda (z) z)))
(define one (lambda (s) (lambda (z) (s z))))
(define succ
  (lambda (n)
    (lambda (s) (lambda (z) (s ((n s) z))))))
(define (church->scheme n)
  ((n (lambda (x) (+ x 1))) 0))

(church->scheme zero)
(church->scheme one)
(church->scheme (succ (succ (succ (succ (succ one))))))
