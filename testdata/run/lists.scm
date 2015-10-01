(define (fold-right op init lst)
  (if (null? lst)
      init
      (op (car lst) (fold-right op init (cdr lst)))))

(define (fold-left op init lst)
  (if (null? lst)
      init
      (fold-left op (op init (car lst)) (cdr lst))))

(define (append list1 list2)
  (if (null? list1)
      list2
      (cons (car list1) (append (cdr list1) list2))))

(define (reverse1 lst)
  (fold-right (lambda (x y) (append y (cons x '()))) '() lst))

(define (reverse2 lst)
  (fold-left (lambda (x y) (cons y x)) '() lst))

(reverse1 '())
(reverse1 '("eddie" "moe" "jack"))
(reverse2 '("l" "i" "s" "a" "b" "o" "n" "e" "t" "a" "t" "e" "n" "o" "b" "a" "s" "i" "l"))
(reverse2 '(1 2 () (3) ((4))))
