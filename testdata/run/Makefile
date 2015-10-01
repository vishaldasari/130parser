INFILES = $(shell ls *.scan_out)
all: $(INFILES:.scan_out=.out_scan)

%.out_scan: %.scan_out
	@echo cp ${<} ${@}
