import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ScriptService} from '../../../../core/services/script/script.service';
import {ScriptViewerService} from '../../services/script-viewer.service';

@Component({
  selector: 'app-script-read',
  templateUrl: './script-read.component.html',
  styleUrls: ['./script-read.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptReadComponent implements OnInit {
  loading = true;
  error = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private scriptService: ScriptService,
    private scriptViewerService: ScriptViewerService
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      const handleNotFound = () => {
        this.error = 'Skript konnte nicht gefunden werden.';
        this.loading = false;
      };
      if (isNaN(id)) {
        handleNotFound();
      } else {
        this.scriptService.getOne(id).subscribe({
          next: (script) => {
            this.scriptViewerService.setScript(script);
            this.loading = false;
          },
          error: handleNotFound
        });
      }
    });
  }
}
