import { Component, OnInit } from '@angular/core';
import { SimpleScript } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { ActivatedRoute } from '@angular/router';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';

@Component({
  selector: 'app-script-rehearsal-sections',
  templateUrl: './script-rehearsal-sections.component.html',
  styleUrls: ['./script-rehearsal-sections.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptRehearsalSectionsComponent implements OnInit {
  getLoading = true;
  script: SimpleScript = null;
  sections: SimpleSection[] = [
    new SimpleSection('Abschnitt 1', 49, 200),
    new SimpleSection('Abschnitt 2', 0, 600),
    new SimpleSection('Abschnitt 3', 600, 900)
  ];

  constructor(
    public scriptViewerService: ScriptViewerService,
    private scriptService: ScriptService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      this.scriptService.getOne(id).subscribe({
        next: (script) => {
          this.scriptViewerService.setScript(script);
          this.getLoading = false;
        },
        error: () => {
          // TODO: show error
          //this.getError = 'Skript konnte nicht gefunden werden.';
          this.getLoading = false;
        }
      });
    });
  }
}
